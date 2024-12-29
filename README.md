
# Interactive Pom Manager

This is an in-development backend service for extracting, modifying and upgrading the dependencies from pom.xml of a maven project, both direct and transitive.

### Currently it supports the extraction of dependencies from <dependencies>, <dependencyManagement> and plugins extraction from the <plugins> tag.



## Objective
* Extraction of dependencies
* Upgradation/Modification of the dependencies 
* Rebuilding and sending the upgraded/modified pom.xml as a response.
## API Reference

#### POST extraction of dependencies and plugins

```http
POST /api/extract-pom-dependencies
```

| Parameter  | Type   | Description                                                                 |
| :--------- | :----- | :-------------------------------------------------------------------------- |
| `file`     | `file` | **Required**. Use `form-data` for the request body. Set the key to "file", type to "File", and upload the desired `pom.xml` file. |


## Run Locally


*Note: Apache maven should be installed and it's path should be set globally to run the project locally.*

Clone the project

```bash
  git clone https://github.com/saurav3232/ipm.git
```

Start the application.

#### For local run if maven is not installed or configured, you can create a docker image from the dockerfile and run it in a container.(please follow the steps in the title: ```Follow the below if there is a issue pulling the image```)


## Installation

## Easiest way:

### Running via docker
*Make sure Docker desktop is installed*

In the terminal run the following commands:

```bash
docker pull saurav3232/ipm
```

The above command will pull the image for the repo.

```bash
docker run -p 8080:8080 saurav3232/ipm 
```
The above command will spin up the docker container at port 8080 for the image. Now you can send the api requests at port 8080.

### Follow the below if there is a issue pulling the image

*Also requires docker desktop to be installed*


1) Clone the project:
```bash
git clone https://github.com/saurav3232/ipm.git
```
2) Go to the project:
```bash
cd ipm
```
3) Build the docker image:
```bash
docker build -t ipm .
```
4) Run the image in the container: 
```bash
docker run -p 8080:8080 ipm 
```
Now you can use the api.

#### Imp point: If you are using a custom repository source for maven, then you need to add the settings.xml file in the project folder and uncomment the following line from the dockerfile:
```bash
COPY settings.xml /root/.m2/settings.xml
```







    
