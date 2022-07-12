:: Build the image
:: TODO
call ./gradlew clean build
call docker buildx create
call docker build --platform linux/amd64 .
call docker build --load .
call docker build -t registry.heroku.com/eist-h10e01/web .
call docker buildx stop
call docker buildx rm
call docker push registry.heroku.com/eist-h10e01/web

:: Release on Heroku
:: TODO
call heroku container:release web -a eist-h10e01


:: Optional: Remove the image locally
call docker rmi registry.heroku.com/eist-h10e01/web
