# Scorewarrior Asset Packaging Pipeline

Author: Vishwanathan K.H.

## 🧩 Overview

This repository implements a TeamCity CI/CD pipeline for assembling and uploading game asset packages.

## 📁 Structure

```
.
├── assets/                     # Source image assets and bundles (add this yourself)
├── assembled_assets/          # Output ZIPs (auto-generated)
├── scripts/
│   ├── package_assets.py      # Python script to package assets
│   └── upload_to_gcs.sh       # Bash script to upload archives to GCS
├── .teamcity/
│   ├── settings.kts           # TeamCity project configuration
│   ├── AssetAssembly.kts      # Build 1: Asset assembly
│   ├── AssetUpload.kts        # Build 2: Upload to GCS
│   └── AssetPipeline.kts      # Build 3: Composite pipeline
```

## 🔨 Build Pipeline

### 1. Asset Assembly
- Packages solo images and bundles into MD5-named ZIPs.
- Bundles are rotated as per JSON config (`rotate`: `left`, `right`, `none`, `no​ne`).

### 2. Upload to GCS
- Uploads ZIPs to the specified GCS bucket.
- Only runs on the `main` branch.
- Uses service account JSON credentials passed as parameter.

### 3. Composite Build
- Triggers AssetAssembly → AssetUpload.
- Automatically triggered on any branch push.

## ⚙️ Required Parameters (TeamCity)
- `env.BUCKET_NAME` — GCS bucket name
- `env.CREDENTIALS_JSON` — Inline GCP service account JSON

## 🚀 Usage

```bash
# Local test
python3 scripts/package_assets.py
./scripts/upload_to_gcs.sh <bucket-name> "$(cat creds.json)" assembled_assets
```

## ✅ Prerequisites (Agent)
- Python 3.9+
- Pillow (`pip3 install pillow`)
- `gcloud`, `zip`, `unzip`, `imagemagick`
