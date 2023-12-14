import requests
import json

# Beispiel-URL zur NetLicensing-API
api_url = "https://www.netlicensing.io/core/v2/rest/"

# Hier fügst du deinen eigenen Zugriffsschlüssel (API-Schlüssel) ein
api_key = "DEIN_API_SCHLUESSEL"

# Beispiel-Endpunkt, um Lizenzinformationen abzurufen
endpoint = "license"

headers = {
    "Accept": "application/json",
    "Authorization": f"Basic {api_key}"
}

# Anfrage an die NetLicensing-API senden
response = requests.get(f"{api_url}/{endpoint}", headers=headers)

if response.status_code == 200:
    license_info = response.json()
    # Hier kannst du die Lizenzinformationen weiterverarbeiten
    print(json.dumps(license_info, indent=2))
else:
    print(f"Fehler beim Abrufen der Lizenzinformationen. Statuscode: {response.status_code}")
