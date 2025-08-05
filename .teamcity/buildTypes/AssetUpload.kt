package buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*

object AssetUpload : BuildType({
    name = "Upload Assets to GCS"

    vcs {
        root(DslContext.settingsRoot)
    }

    requirements {
        equals("teamcity.build.branch", "main")
    }

    dependencies {
        artifacts(AssetAssembly) {
            artifactRules = "assembled_assets.zip!** => ."
        }
    }

    params {
        param("env.BUCKET_NAME", "your-bucket-name-here")
        param("env.CREDENTIALS_JSON", "your-json-content")
    }

    steps {
        script {
            name = "Unzip Artifacts"
            scriptContent = "unzip -o assembled_assets.zip -d assembled_assets"
        }
        script {
            name = "Upload to GCS"
            scriptContent = "./scripts/upload_to_gcs.sh %env.BUCKET_NAME% "%env.CREDENTIALS_JSON%" assembled_assets"
        }
    }
})
