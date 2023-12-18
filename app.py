from io import BytesIO
import pycurl
import sys
import json

# pip install json
# pip install pycurl

api_url = "https://go.netlicensing.io/core/v2/rest/licensee/" + \
    sys.argv[1] + "/validate"

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
y = json.loads(response)
y = y['items']['item']
y = y[0]['list']

changeEntry = y[0]['property']
changeEntry = changeEntry[0]['value']

RegisterKids = y[1]['property']
RegisterKids = RegisterKids[0]['value']

DownloadExcel = y[2]['property']
DownloadExcel = DownloadExcel[0]['value']

sys.stdout.write("changeEntry@" + changeEntry + "@RegisterKids@" +
                 RegisterKids + "@DownloadExcel@" + DownloadExcel)
