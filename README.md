# Log4j2 Masking Policy

A Log4j2 Rewrite policy to mask sensitive data in XML and JSON.

# Installation

Add the following dependency to the project:
```xml
<dependency>
    <groupId>com.mariocairone</groupId>
    <artifactId>log4j2-masking-policy</artifactId>
    <version>1.0.1</version>
</dependency>
```


# Configuration

## MaskPolicies
Wrapper for one or more MaskPolicy elements.

## Converters
Wrapper for one or more Converter elements.

### Converter
Convert are used to transform the log4j2 `Message` into string before masking and convert the masked string back to log4j2 `Message`.
The Converter class must implement the `MessageConverter` interface.

| attribute | values                     | description             | required |
| --------- | -------------------------- | ----------------------- | -------- |
| className | Fully qualified class name | Specify formatter class | true     |

###### Example
```xml
    ...
    <MaskPolicies>
        <Converters>
            <Converter className="com.mycompany.MyMessageConverter" />
        </Converters>
        <MaskPolicy type="JSON" enabled="true"> 
            <Exclusions>
                <Exclusion value="$.store.book[*].author" />
            </Exclusions>
        </MaskPolicy>				
    </MaskPolicies>
    ...        
```

## MaskPolicy

| attribute | values     | description                   | required |
| --------- | ---------- | ----------------------------- | -------- |
| type      | XML/JSON   | Specify the type of Masker    | true     |
| enabled   | true/false | Enable or disable the masking | false    |


### Exclusions
Wrapper for one or more Exlusion elements.

#### Exclusion
Specify the element that shouldn't be masked.
The value attribute can be a simple string or a path.
If a single string is specified, all the `nodes/elements/attributes` having that string as key/name will not be masked.

##### JSON
 
| attribute | MaskPolicyType | values          | required |
| --------- | -------------- | --------------- | -------- |
| value     | JSON           | String/JsonPath | true     |

###### Example
```xml
    ...
    <MaskPolicies>
        <MaskPolicy type="JSON" enabled="true"> 
            <Exclusions>
                <Exclusion value="$.name" />
            </Exclusions>
        </MaskPolicy>				
    </MaskPolicies>
    ...        
```

##### XML

| attribute | values | description                                   |
| --------- | ------ | --------------------------------------------- |
| value     | XPath  | Specify the elements that shouldn't be masked |

###### Example
```xml
    ...
    <MaskPolicies>
        <MaskPolicy type="XML" > 
            <Exclusions>
                <Exclusion value="/customer/order[1]" />
            </Exclusions>
        </MaskPolicy>				
    </MaskPolicies>
    ...        
```

## Log4j2 Complete Configuration Example

Below is an example of log4j2 configuration file.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="debug" packages="com.mariocairone.log4j2">

    <Appenders>
        
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout   pattern="[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n" />
        </Console>

        <Rewrite name="REWRITE">
            <AppenderRef ref="Console" />
            <MaskPolicies>
                <MaskPolicy type="JSON" enabled="true"> 
                    <Exclusions>
                        <Exclusion value="$.name" />
                    </Exclusions>
                </MaskPolicy>				
            </MaskPolicies>
        </Rewrite>   

    </Appenders>

    <Loggers>
        <Root level="debug" additivity="false">
            <appender-ref ref="Console" />
        </Root>
    </Loggers>

</Configuration>
```

---
Note: the package `com.mariocairone.log4j2` must be added in the log4j2 configuration file.