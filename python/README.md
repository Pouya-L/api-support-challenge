# Technical Challenge Python

## Overview
This Python script performs three main tasks using HTTP requests:
1. Searches and displays a list of apps from the Aptoide API.
2. Retrieves and displays the description of a specific app.
3. Downloads the APK file of a specific app.

## Prerequisites

- Python 3.x
- `requests` library: This library is used to make HTTP requests.
- `base64` library: This library is used to encode and decode data in base64 format.
- `json` library: This library is used to parse JSON data.

## Usage
Clone the repository or download the script.
Navigate to the directory containing the script.
Run the script using Python `python app.py`

### Script details

**Step 1**: Search and Display List of Apps
The script performs an HTTP GET request to the following URL to get a list of apps:
   ```
   https://ws75.aptoide.com/api/7/apps/get/store_name=apps/q=bWF4U2RrPTE5Jm1heFNjcmVlbj1ub3JtYWwmbWF4R2xlcz0yLjA/group_name=games/limit=10/offset=0/mature=false
   ```
It then parses the JSON response and uses .get() to safely access nested dictionaries and provide default values if keys are missing. Then in a for loop it loops through each app in the list of app and prints their names.

**Step 1.1**: Decode the "q" parameter

`encoded_q`: This is the base64 encoded "q" parameter from the URL. Base64 strings should have a length that is a multiple of 4.
Since it doesn't meet that criteria, we need to add the appropriate number of = characters to the end of the string to make the length a multiple of 4.

`base64.b64decode(encoded_q).decode('utf-8')`: This decodes the base64 string and converts it to a UTF-8 string.
A **UTF-8** string is a sequence of characters encoded using the UTF-8 (Unicode Transformation Format - 8-bit) encoding



**Step 2**: Search and Display List of Apps
The script performs an HTTP GET request to the following URL to get the details of a specific app:
   ```
   https://ws75.aptoide.com/api/7/app/get/store_name=apps/q=bWF4U2RrPTE5Jm1heFNjcmVlbj1ub3JtYWwmbWF4R2xlcz0yLjA/package_name=com.fun.lastwar.gp/language=pt_PT/
   ```
It then parses the JSON response. First we print the entire JSON response for debugging `print(json.dumps(data, indent=4))` through that we find out that the description is nested under nodes -> meta -> data -> media -> description. Using .get() the script extracts the app description from the JSON response.

**Step 3:** Download App File (APK)
when we try to access the original url we recieve this error : `{"errors": [{"type": "missing", "loc": ["aptoide_uid"], "msg": "Field required"}]}` showing us that aptoide_uid parameter is missing, so we add this parameter with a dummy value to the end of the link
The script performs an HTTP GET request to the following URL to download the APK file of a specific app:
   ```
   https://aptoide-mmp.aptoide.com/api/v1/download/b2VtaWQ9VGVjaENoYWxsZW5nZVB5dGhvbiZwYWNrYWdlX25hbWU9Y29tLmZ1bi5sYXN0d2FyLmdwJnJlZGlyZWN0X3VybD1odHRwczovL3Bvb2wuYXBrLmFwdG9pZGUuY29tL2FwcHMvY29tLWZ1bi1sYXN0d2FyLWdwLTk5OTk5LTY2NjEyOTMwLWE3MThmOWZlMjE5OGM1Y2EyYzIwMmUwNDYzZTVkZDk1LmFwaw==?resolution=1080x1776&aptoide_uid=dummy_value
   ```
`with open("app.apk", "wb") as file`: This opens a file named app.apk in write-binary mode.and then we write the content of the response (the APK file) to the file essentially downloading the apk.
