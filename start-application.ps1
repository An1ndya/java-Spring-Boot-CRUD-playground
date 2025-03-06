# PowerShell script to check for and kill processes using ports 8080, 8081, and 8082, then start the application on port 8080

# Function to check and kill processes on a specific port
function Kill-ProcessOnPort {
    param (
        [int]$Port
    )
    
    Write-Host "Checking if port $Port is in use..." -ForegroundColor Yellow
    
    # Find processes using the specified port
    $netstatOutput = netstat -ano | findstr ":$Port" | findstr "LISTENING"
    
    if ($netstatOutput) {
        Write-Host "Found process using port $Port" -ForegroundColor Yellow
        
        # Extract the PID using a more reliable method
        $netstatOutput -match "\s+(\d+)$" | Out-Null
        
        if ($matches -and $matches[1]) {
            $processPID = $matches[1]
            
            Write-Host "Found process with PID: $processPID using port $Port" -ForegroundColor Yellow
            
            try {
                # Get the process name
                $process = Get-Process -Id $processPID -ErrorAction Stop
                $processName = $process.ProcessName
                Write-Host "Process name: $processName" -ForegroundColor Yellow
                
                # Kill the process
                Write-Host "Stopping process with PID: $processPID" -ForegroundColor Yellow
                Stop-Process -Id $processPID -Force
                
                # Wait a moment to ensure the port is released
                Write-Host "Waiting for port to be released..." -ForegroundColor Yellow
                Start-Sleep -Seconds 2
            }
            catch {
                Write-Host "Error processing PID $processPID`: $_" -ForegroundColor Red
            }
        } else {
            Write-Host "Could not extract PID from netstat output" -ForegroundColor Red
        }
        
        # Double-check if port is still in use
        $stillInUse = netstat -ano | findstr ":$Port" | findstr "LISTENING"
        if ($stillInUse) {
            Write-Host "Port $Port is still in use. Using taskkill to force close all processes..." -ForegroundColor Yellow
            # Use the Windows command to forcefully kill any process on the port
            $result = cmd /c "for /f `"tokens=5`" %a in ('netstat -ano ^| findstr :$Port ^| findstr LISTENING') do taskkill /F /PID %a"
            Write-Host $result
            Start-Sleep -Seconds 2
        }
    } else {
        Write-Host "No process found using port $Port" -ForegroundColor Green
    }
}

# Kill processes on ports 8081 and 8082 first
Kill-ProcessOnPort -Port 8081
Kill-ProcessOnPort -Port 8082

# Then check and kill any process on port 8080 to ensure it's free
Kill-ProcessOnPort -Port 8080

# Ensure application.properties has the correct port
Write-Host "Ensuring application uses port 8080..." -ForegroundColor Yellow
$appPropsPath = "src\main\resources\application.properties"
$content = Get-Content $appPropsPath
$newContent = $content -replace "server.port=.*", "server.port=8080"
$newContent | Set-Content $appPropsPath

# Start the application
Write-Host "Starting application on port 8080..." -ForegroundColor Green
mvn spring-boot:run
