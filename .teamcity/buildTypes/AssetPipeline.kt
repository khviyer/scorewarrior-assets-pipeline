package buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*

object AssetPipeline : BuildType({
    name = "Asset Build Pipeline"

    type = BuildTypeSettings.Type.COMPOSITE

    vcs {
        root(DslContext.settingsRoot)
    }

    triggers {
        vcs {
            branchFilter = "+:*"
        }
    }

    dependencies {
        snapshot(AssetAssembly) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
        snapshot(AssetUpload) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
    }

    artifactRules = "assembled_assets.zip!** => final_assets"
})
