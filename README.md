# featureFlags

A config system to get and update feature flags

Since most the config system we use ties us to pay for things we are not using. So this config system helps us to use our existing system to provide us the required data and use that. Do fork it as per your organization needs.

features: Accepts Json Data, Looks for cached file as a latest data, uses Room to store data.

# Screenshots

![Screenshot_20230703-104942](https://github.com/bagadesh/featureFlags/assets/48888901/047e3a69-5359-46de-b7ef-944a14e30955)
![Screenshot_20230703-104936](https://github.com/bagadesh/featureFlags/assets/48888901/a1cd0b83-e0f4-404b-b190-f8ebbbe86b25)

# How it works
## how to initialize the SDK
```Kotlin
// Kindly call this in Application's onCreate
FeatureFlagSdkImpl.init( 
   context = applicationContext,
   defaultConfigResource = R.raw.feature_flag,
   cachedFileName = "feature_flags_cache.json" // Optional with default value
)

```

## how to get the SDK
```kotlin

    val version = "1.0.0-beta03" // Kindly check the latest version from release page
    implementation("com.github.bagadesh.featureFlags:core:$version")
    implementation("com.github.bagadesh.featureFlags:data:$version")
    implementation("com.github.bagadesh.featureFlags:ui:$version") // Optional if you want to show debug menu for your debug when you can toggle or change variables

```


## how to get & use the reader
```Kotlin
private val reader = FeatureFlagSdkImpl.getReader()

reader.isFeatureEnabled(key = "feature_name", default = false) // default is optional

reader.getVariableValue(featureKey = "feature_name", key = "variable_key", default = { "default" }, String::class)
```

## how to get & use the writer
```Kotlin
private val writer = FeatureFlagSdkImpl.getWriter()

writer.enableFeature(key = "feature_name", enabled = true, source = Source.USER)

writer.changeVariableValue(
    request = ChangeVariableRequest(
        featureKey = "feature_name",
        variableKey = "variable_key",
        currentValue = "currentValue",
        changedValue = "changedValue",
        valueType = "string",
    ),
    source = Source.USER
)

```

## how to add the json data

```json
{
    "featureConfigs": { // Default key
        "appOverride": { // Feature Flag Key Name
            "enabled": true,
            "variables": { // Variable default key
                "test": { // Variable Key Name
                    "value": "something", 
                    "value_type": "string" // Represents the type of supported variable
                }
            }
        },
    }
}

```

## Supported variable types
``` "string", "integer", "double", "float", "json_array", "json_object" ```

Here for example ```json_array``` should be retried using ```List<T>```

## how to update json data

While initialising the SDK we can pass optional parameter ```cachedFileName```. SDK will look for the this file in the cache & try to use on every initialization.

Currently, it will be the responsitbility of SDK user to retrive their latest data and push it to the given file name.

## how to use the Feature Flag Screen

```kotlin
val vm = FeatureFlagViewModel(reader = FeatureFlagSdkImpl.getReader(), writer = FeatureFlagSdkImpl.getWriter())
FeatureFlagScreenUi(viewModel = vm)
```

