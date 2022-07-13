# Add base image
FROM openjdk:17-bullseye
# Set workdir
WORKDIR /app
# Copy the compiled jar
COPY ./build/libs/H10E01-Containers-1.0.0.jar /app
# Copy the start.sh script
COPY start.sh /app
RUN chmod 700 start.sh
# Make start.sh executable
# Set the start command
CMD ./start.sh

