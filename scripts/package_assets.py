#!/usr/bin/env python3
# Author: Vishwanathan K.H.
# Asset Packaging Script

import hashlib
import json
import shutil
from pathlib import Path
from PIL import Image

assets_dir = Path("assets")
output_dir = Path("assembled_assets")
output_dir.mkdir(exist_ok=True)

def md5sum(file_path):
    hash_md5 = hashlib.md5()
    with open(file_path, "rb") as f:
        for chunk in iter(lambda: f.read(4096), b""):
            hash_md5.update(chunk)
    return hash_md5.hexdigest()

def rotate_image(image, direction):
    direction = direction.strip().lower()
    if direction == "left":
        return image.rotate(90, expand=True)
    elif direction == "right":
        return image.rotate(-90, expand=True)
    elif direction in {"none", "no​ne"}:
        return image
    else:
        raise ValueError(f"Unknown rotation: {direction}")

def package_assets():
    count = 0
    for file_path in assets_dir.rglob("*.*"):
        if file_path.suffix.lower() not in [".png", ".jpg", ".jpeg"]:
            continue

        base_name = file_path.stem
        json_path = file_path.with_suffix(".json")
        temp_dir = output_dir / f"tmp_{count}"
        temp_dir.mkdir(exist_ok=True)

        if json_path.exists():
            with open(json_path) as f:
                config = json.load(f)
            rotate_dir = config.get("rotate", "none")
            img = Image.open(file_path)
            img = rotate_image(img, rotate_dir)
            png_output = temp_dir / f"{base_name}.png"
            img.save(png_output)
            shutil.copy(json_path, temp_dir / json_path.name)
        else:
            shutil.copy(file_path, temp_dir / file_path.name)

        zip_temp_path = output_dir / f"{count}.zip"
        shutil.make_archive(str(zip_temp_path).replace(".zip", ""), 'zip', temp_dir)
        final_md5 = md5sum(zip_temp_path)
        final_zip_path = output_dir / f"{final_md5}.zip"
        zip_temp_path.rename(final_zip_path)

        shutil.rmtree(temp_dir)
        count += 1

package_assets()
