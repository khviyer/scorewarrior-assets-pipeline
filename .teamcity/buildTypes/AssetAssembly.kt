package buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*

object AssetAssembly : BuildType({
    name = "Asset Assembly"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            name = "Install dependencies"
            scriptContent = "pip3 install pillow"
        }
        script {
            name = "Run Asset Packager"
            scriptContent = "python3 scripts/package_assets.py"
        }
    }

    artifactRules = "assembled_assets/*.zip => assembled_assets.zip"
})
