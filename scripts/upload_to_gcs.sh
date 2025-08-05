#!/bin/bash
# Author: Vishwanathan K.H.
# Script to upload asset zip files to Google Cloud Storage

set -e

# --- Parameters ---
BUCKET_NAME=$1
CREDENTIALS_JSON=$2
ASSETS_DIR=$3

if [[ -z "$BUCKET_NAME" || -z "$CREDENTIALS_JSON" || -z "$ASSETS_DIR" ]]; then
  echo "Usage: $0 <bucket-name> <credentials-json-path> <assets-dir>"
  exit 1
fi

# Auth with service account
echo "$CREDENTIALS_JSON" > /tmp/gcs-creds.json
gcloud auth activate-service-account --key-file=/tmp/gcs-creds.json

# Upload all .zip files
for file in "$ASSETS_DIR"/*.zip; do
  echo "Uploading $file to gs://$BUCKET_NAME/"
  gcloud storage cp "$file" "gs://$BUCKET_NAME/"
done

echo "✅ All files uploaded successfully."
