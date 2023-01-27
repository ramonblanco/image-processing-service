# team-seven-code-challenge
Repository for Microservice Code Challenge for team number 7 

# Requirements

- Java 19
- Maven 
- MySQL

___
# How to run the project
Follow the next document to set up the project: https://docs.google.com/document/d/1mXXfVi7RCs6Y5tTNBfkqNsVraMjp94K9X8APK0PkFdw/edit?usp=sharing

___

# API endpoints
These endpoints allow you to upload, search and check the status of an image

## POST
`http://localhost:8080/image` [?folder=x&title=x&description=x](#post-image)

## GET
`http://localhost:8080/image` [?imageId=x](#get-imagestatus) <br/>
`http://localhost:8080/image/getByDates` [?beginDate=x&endDate](#get-image)

___

### POST /image
This endpoint process a received image, it saves a reference of the image in the database, uploads the image to a Drive folder and send the image to the listed globant mails.

**Parameters**

|    Name     | Required |       Type       |                                                                     Description                                                                     |
|:-----------:|:---------:|:----------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------:|
|   folder    | required  |      string      |                                 cUrl parameter. <br/> Name of the Google drive folder where the image will be save                                  |
|    title    | required  |      string      |                                             cUrl parameter. <br/> New title that will receive the image                                             |
| description | required  |      string      |                                                   cUrl parameter. <br/> Description for the image                                                   |
|    file     | required  |  MultiPartFile   |                                                Form parameter. <br/> Image file that will be process                                                |
| targetEmails | required  | Array of strings | Header parameter. <br/> List of mails that the image will be send to. <br/> All the images must follow the globant mail format (foo@`globant.com`). |

___
### GET /image
This endpoint returns the status of an image search by its ID. The possible status for an image are `In process` `Completed` `Failed`

**Parameters**

|   Name    | Required |       Type       |                        Description                         |
|:---------:|:---------:|:----------------:|:----------------------------------------------------------:|
| imageID   | required  |      string      | cUrl parameter. <br/> ID of the desired image to search    |

---
### GET /image/getByDates
This endpoint returns a list of image files that where uploaded between the range of dates specified. In case there was no image found, it will return an empty list.

**Parameters**

|   Name    | Required | Type |                                         Description                                         |
|:---------:|:---------:|:----:|:-------------------------------------------------------------------------------------------:|
| beginDate | required  | Date | cUrl parameter. <br/> Begin date for the search. <br/> Format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX |
|  endDate  | required  | Date |  cUrl parameter. <br/> End date for the search <br/> Format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX   |
