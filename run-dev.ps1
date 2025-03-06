# PowerShell script to run the application with dev profile

Write-Host "Checking for processes using port 8081..."
$processInfo = netstat -ano | findstr :8081
if ($processInfo) {
    $processId = ($processInfo -split ' ')[-1]
    Write-Host "Killing process $processId that is using port 8081"
    taskkill /F /PID $processId
}

Write-Host "Starting application with dev profile..."
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
