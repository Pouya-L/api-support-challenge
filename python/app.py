import requests
import base64
import json

### PART 1
# URL for searching apps
search_url = "https://ws75.aptoide.com/api/7/apps/get/store_name=apps/q=bWF4U2RrPTE5Jm1heFNjcmVlbj1ub3JtYWwmbWF4R2xlcz0yLjA/group_name=games/limit=10/offset=0/mature=false"

# Perform HTTP GET request
response = requests.get(search_url)
if response.status_code == 200:
	data = response.json()
	# Display list of apps
	apps = data.get('datalist', {}).get('list', [])
	for app in apps:
		print(app.get('name'))
else:
	print("Failed to retrieve apps")

# Decode the "q" parameter
encoded_q = "bWF4U2RrPTE5Jm1heFNjcmVlbj1ub3JtYWwmbWF4R2xlcz0yLjA"
# Add padding to the base64 string if necessary
missing_padding = len(encoded_q) % 4
if missing_padding:
	encoded_q += '=' * (4 - missing_padding)
decoded_q = base64.b64decode(encoded_q).decode('utf-8')
print("Decoded 'q' parameter:", decoded_q)

### PART 2
# URL for app details for com.fun.lastwar.gp
app_details_url = "https://ws75.aptoide.com/api/7/app/get/store_name=apps/q=bWF4U2RrPTE5Jm1heFNjcmVlbj1ub3JtYWwmbWF4R2xlcz0yLjA/package_name=com.fun.lastwar.gp/language=pt_PT/"

# Perform HTTP GET request
response = requests.get(app_details_url)
if response.status_code == 200:
	data = response.json()

	# Print the entire JSON response for debugging
	# print(json.dumps(data, indent=4))
	# Now that we have the JSON response and can see the structure, we can correctly extract the app description. The description is nested under nodes -> meta -> data -> media -> description.

	# Display app description, it will be in Portuguese matching the language specified in the URL (language=pt_PT).
	description = data.get('nodes', {}).get('meta', {}).get('data', {}).get('media', {}).get('description', '')
	print("App Description:", description)
else:
	print("Failed to retrieve app details")

### PART 3
# when we try to access the url we receive this error : {"errors": [{"type": "missing", "loc": ["aptoide_uid"], "msg": "Field required"}]}
# the error message indicates that the aptoide_uid parameter is missing from the URL. We need to add this parameter with a dummy value to the URL. so we add &aptoide_uid=dummy_value.

download_url = "https://aptoide-mmp.aptoide.com/api/v1/download/b2VtaWQ9VGVjaENoYWxsZW5nZVB5dGhvbiZwYWNrYWdlX25hbWU9Y29tLmZ1bi5sYXN0d2FyLmdwJnJlZGlyZWN0X3VybD1odHRwczovL3Bvb2wuYXBrLmFwdG9pZGUuY29tL2FwcHMvY29tLWZ1bi1sYXN0d2FyLWdwLTk5OTk5LTY2NjEyOTMwLWE3MThmOWZlMjE5OGM1Y2EyYzIwMmUwNDYzZTVkZDk1LmFwaw==?resolution=1080x1776&aptoide_uid=dummy_value"

# Perform HTTP GET request to download APK
response = requests.get(download_url)
if response.status_code == 200:
	with open("app.apk", "wb") as file:
		file.write(response.content)
	print("APK downloaded successfully")
else:
	print("Failed to download APK")
