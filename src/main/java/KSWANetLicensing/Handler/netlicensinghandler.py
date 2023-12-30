from io import BytesIO
import pycurl
import sys
import json

# pip install json
# pip install pycurl

# Programmstart: python app.py Lizenznummer
# I7KI9KY9S Lizenznummer

api_url = "https://go.netlicensing.io/core/v2/rest/licensee/" + \
    sys.argv[1] + "/validate" # api url mit Lizenznummer die als Argument beim Programmstart übergeben wurde

# imports here
headers = ["Authorization: Basic ZGVtbzpkZW1v", "Accept: application/json"]
buffer = BytesIO()
c = pycurl.Curl()
c.setopt(c.URL, api_url)
c.setopt(c.HTTPHEADER, headers)
c.setopt(c.WRITEDATA, buffer)
c.perform()
c.close()
response = buffer.getvalue()
# json_string = response.decode("utf-8")
json_object = json.loads(response)

json_object_info = json_object['infos']['info']
json_object_info = json_object_info[0]['id']
if(json_object_info == 'NotFoundException'):
    sys.stdout.write("invalid")
    exit(1)

#print(json.dumps(json_object, indent=2))

### Licencing
json_object_item = json_object['items']['item']
json_object_list = json_object_item[0]['list']

RegisterKids = json_object_list[0]['property']
#print(json.dumps(RegisterKids, indent=2))
RegisterKidsValid = RegisterKids[0]['value']

changeEntry = json_object_list[1]['property']
#print(json.dumps(changeEntry, indent=2))
changeEntryValid = changeEntry[0]['value']

DownloadExcel = json_object_list[2]['property']
#print(json.dumps(DownloadExcel, indent=2))
DownloadExcelValid = DownloadExcel[0]['value']

licenceString = "changeEntry@" + changeEntryValid + "@RegisterKids@" + RegisterKidsValid + "@DownloadExcel@" + DownloadExcelValid
sys.stdout.write(licenceString)