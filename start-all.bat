@echo off
cls
echo ============================================
echo   PetVerse - Tum Servisleri Derleyici       
echo ============================================

echo.
echo [1] Docker servisleri kapatiliyor...        
docker compose down

:: Fonksiyon: Maven build için alt klasörlere gir
set SERVICES=activityservice apigateway configserver eurekaserver notificationservice petservice userservice

echo.
echo [2] Tum servisler Maven ile derleniyor...
for %%S in (%SERVICES%) do (
    echo --------------------------------------------
    echo Derleniyor: %%S
    echo --------------------------------------------
    cd %%S
    call mvn clean install -DskipTests
    if %errorlevel% neq 0 (
        echo [HATA] %%S derlenirken hata olustu!
        pause
        exit /b %errorlevel%
    )
    cd ..
)

echo.
echo ============================================
echo [✓] Tum servisler basariyla derlendi.
echo.
echo [Not] Docker imajlarini build edip baslatmak icin su komutu manuel calistirin:
echo docker compose up --build
echo ============================================

pause
