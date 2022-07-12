# Add base image
FROM openjdk:17-bullseye
# Set workdir
WORKDIR /app
# Copy the compiled jar
ADD build/libs/H10E01-Containers-1.0.0.jar /app
# Copy the start.sh script
ADD start.sh /app
# Make start.sh executable
# Set the start command
CMD ./start.sh