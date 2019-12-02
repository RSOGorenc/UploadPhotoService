# UploadPhotoService
Microservice for photo upload service

Launch mongo: $ docker run -d --name mongodb -p 27017:27017 mongo

Env variables required for AWS:

1. AWS_ACCESS_KEY_ID
1. AWS_SECRET_ACCESS_KEY

Testiranje:

Preko naslova http://104.199.9.130/v1/upload poslji post request s sledecim Json objektom:

{
    "imageBase64":"<base64 zakodirana slika>"
    "userId":"<id_uporabnika>"
}