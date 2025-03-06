# PowerShell script to run the application with prod profile

Write-Host "Checking for processes using port 8080..."
$processInfo = netstat -ano | findstr :8080
if ($processInfo) {
    $processId = ($processInfo -split ' ')[-1]
    Write-Host "Killing process $processId that is using port 8080"
    taskkill /F /PID $processId
}

Write-Host "Starting application with prod profile..."
mvn spring-boot:run "-Dspring-boot.run.profiles=prod"
