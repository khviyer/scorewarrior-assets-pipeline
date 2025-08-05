import jetbrains.buildServer.configs.kotlin.v2019_2.*
import buildTypes.*

object Project : Project({
    id("ScorewarriorAssetPackaging")
    name = "Scorewarrior Assets Pipeline"

    buildType(AssetAssembly)
    buildType(AssetUpload)
    buildType(AssetPipeline)

    buildTypesOrder = arrayListOf(AssetPipeline, AssetAssembly, AssetUpload)
})
