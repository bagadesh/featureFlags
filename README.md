# featureFlags
A config system to get and update feature flags

Since most the config system we use ties us to pay for things we are not using. So this config system helps us to use our existing system to provide us the required data and use that.

# Screenshots

![Screenshot_20230703-104942](https://github.com/bagadesh/featureFlags/assets/48888901/047e3a69-5359-46de-b7ef-944a14e30955)
![Screenshot_20230703-104936](https://github.com/bagadesh/featureFlags/assets/48888901/a1cd0b83-e0f4-404b-b190-f8ebbbe86b25)

# How it works
## how to initialize the SDK
```Kotlin
FeatureFlagSdkImpl.init(
   context = applicationContext,
   defaultConfigResource = R.raw.feature_flag,
)

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

```JSON
{
    "someRandomStringKey": "someRandomStringValue",
    "someRandomIntKey": 1,
    "featureConfigs": {
        "appOverride": {
            "enabled": true,
            "variables": {
                "test": {
                    "value": "something",
                    "value_type": "string"
                }
            }
        },
    }
}

```

## Supported variable types
``` "string", "integer", "double", "float", "json_array", "json_object", "" ```

Here for example ```json_array``` should be retried using ```List<T>```

## how to update json data

While initialising the SDK we can pass optional parameter ```cachedFileName```. SDK will look for the this file in the cache & try to use on every initialization.

Currently, it will be the responsitbility of SDK user to retrive their latest data and push it to the given file name.

