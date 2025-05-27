For the Chinese version of this document, please see [README_zh.md](./README_zh.md).

---
# Yapi MCP Server

This project uses Spring Boot Native Image to build a native executable and a minimal Docker image.

## Prerequisites

*   Docker installed and running.
*   Maven installed (for building the image, if you choose to build it yourself).

## Building the Docker Image (Optional)

The pre-built image is `yukaifan/yapi-mcp-server`. If you want to build the image yourself, use the following command:

```bash
mvn -Pnative clean spring-boot:build-image
```
This command will build the native executable and package it into a Docker image. The image name will typically be determined by the project's artifactId and version. You might need to tag it appropriately if you want a specific name like `yukaifan/yapi-mcp-server`.

## Running the Application

1.  **Download Configuration File:**
    Download the `application-mcp.yml` configuration file from:
    [https://raw.githubusercontent.com/yyykf/yapi-mcp-server/refs/heads/master/src/main/resources/application-mcp.yml](https://raw.githubusercontent.com/yyykf/yapi-mcp-server/refs/heads/master/src/main/resources/application-mcp.yml)

    Save this file to a directory on your local machine (e.g., `/path/to/your/config/`).

2.  **Customize Configuration:**
    Open the downloaded `application-mcp.yml` and adjust the settings according to your environment and requirements. This file contains crucial settings for the application to run correctly.

3.  **Run the Docker Container:**
    Execute the following command in your terminal, replacing `/path/to/your/application-mcp.yml` with the actual absolute path to where you saved the configuration file:

    ```bash
    docker run --rm -p 8888:8888 -v /path/to/your/application-mcp.yml:/workspace/application-mcp.yml yukaifan/yapi-mcp-server
    ```
    For example, if you are on Windows and saved the file to `D:\Dev\config\application-mcp.yml`, the command would be:
    ```bash
    docker run --rm -p 8888:8888 -v D:\Dev\config\application-mcp.yml:/workspace/application-mcp.yml yukaifan/yapi-mcp-server
    ```
    If you are on Linux or macOS and saved it to `/home/user/config/application-mcp.yml`, it would be:
    ```bash
    docker run --rm -p 8888:8888 -v /home/user/config/application-mcp.yml:/workspace/application-mcp.yml yukaifan/yapi-mcp-server
    ```

    The application will start, and you should be able to access it at `http://localhost:8888`. The `-v` flag mounts your local configuration file into the container at `/workspace/application-mcp.yml`, which the application expects.

    ### Using Docker Compose

    Alternatively, after downloading and customizing `application-mcp.yml` (as described in steps 1 and 2 above), you can use Docker Compose to manage the container. Ensure the `application-mcp.yml` file is in the same directory as the `docker-compose.yml` file.

    To start the application, run:
    ```bash
    docker-compose up
    ```
    To run in detached mode:
    ```bash
    docker-compose up -d
    ```
    This will use the `docker-compose.yml` file provided in the project, which is pre-configured to use the `yukaifan/yapi-mcp-server` image, map port `8888`, and mount the `application-mcp.yml` file from the current directory.

## Health Check

You can check the application's health by accessing the following endpoint in your browser or using a tool like `curl`:

`http://localhost:8888/actuator/health`

This endpoint should return a status indicating if the application is running correctly.
