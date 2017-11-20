# SkinLib


Change Skin for Andorid and RN


###  USE
 git submodule add https://github.com/wisn-mirror/SkinLib.git /your project/lib

 example code  :https://github.com/wisn-mirror/React-NativeDemo/tree/master/ThemByAndroid

 esample apk  :https://github.com/wisn-mirror/React-NativeDemo/tree/master/ThemByAndroid/android/apkFile/
### ScreenShot

<img width="40%" src="./app/pic/a1.png" />

<img width="40%" src="./app/pic/a2.png" />

<img width="40%" src="./app/pic/a3.png" />

<img width="40%" src="./app/pic/a4.png" />



## 切换皮肤

```java
 设置指定皮肤
 SkinManager.getInstance().loadSkin("theme-com.wisn.skin2--43-1.0-2017-09-22-04-10-49.skin", new SkinLoaderListener());
 恢复默认皮肤
 SkinManager.getInstance().resetDefaultThem();
 夜间模式
 SkinManager.getInstance().nightMode();
 设置夜间模式指定皮肤
 SkinManager.getInstance().loadSkin("theme-com.wisn.skin2--43-1.0-2017-09-22-04-10-49.skin", true,new SkinLoaderListener());

                                              
```
## 加载皮肤文件

```java
 
 SkinManager.getInstance().saveSkin(String skinFilePath,
                            String skinName)
                                               
```
## 切换皮肤路径

```java
 
 SkinManager.getInstance().updateSkinPath(“皮肤根路径”,new SkinLoaderListener());

                                               
```

## 自定义View换肤：

```java

 DrawableTopAttr  drawableTopAttr=new DrawableTopAttr();
 drawableTopAttr.setRes("drawableTop",attrValueresId);
 dynamicAddView(view,drawableTopAttr);
       
```
## 加载字体文件

```java

SkinManager.getInstance().saveFont("字体路径")     
                                          
```
## 切换字体文件

```java

SkinManager.getInstance().loadFont("字体名称" ,new SkinLoaderListener()))     
                                          
```
##  React Native 本地Module：

```java

 	@ReactMethod
    public void getImage(String StateName, String imageName, Callback callback) {
        LogUtils.e("getImage", "StateName:" + StateName + " imageName:" + imageName);
        WritableMap params = Arguments.createMap();
        String path = SkinManager.getInstance().getPathForRN(imageName);
        params.putString(StateName, path);
        callback.invoke(params);
    }

    @ReactMethod
    public void getImageMap(ReadableMap StateName, Callback callback) {
        WritableMap params = Arguments.createMap();
        ReadableMapKeySetIterator readableMapKeySetIterator = StateName.keySetIterator();
        while (readableMapKeySetIterator.hasNextKey()) {
            String s = readableMapKeySetIterator.nextKey();
            String Name=StateName.getString(s);
            String path = SkinManager.getInstance().getPathForRN(Name);
            LogUtils.e("MainModule", "getImageMap path:" + path + "  name:" + Name+ "  key:" + s);
            params.putString(s, path);
        }
        callback.invoke(params);
    }

    @ReactMethod
    public void getImageList(ReadableArray StateName, Callback callback) {
        WritableMap params = Arguments.createMap();
        for (int i = 0; i < StateName.size(); i++) {
            String string = StateName.getString(i);
            String path = SkinManager.getInstance().getPathForRN(string);
            LogUtils.e("MainModule", "getImageList path:" + path + "  name:" + string);
            params.putString(string, path);
        }
        callback.invoke(params);
    }

    @ReactMethod
    public void getColor(String StateName, String colorName, Callback callback) {
        WritableMap params = Arguments.createMap();
        String path = SkinManager.getInstance().getColorForRN(colorName);
        params.putString(StateName, path);
        callback.invoke(params);
    }

    @ReactMethod
    public void getColorMap(ReadableMap StateName, Callback callback) {
        WritableMap params = Arguments.createMap();
        ReadableMapKeySetIterator readableMapKeySetIterator = StateName.keySetIterator();
        while (readableMapKeySetIterator.hasNextKey()) {
            String s = readableMapKeySetIterator.nextKey();
            String Name=StateName.getString(s);
            String path = SkinManager.getInstance().getColorForRN(Name);
            LogUtils.e("MainModule", "getColorMap path:" + path + "  name:" + Name+ "  key:" + s);
            params.putString(s, path);
        }
        callback.invoke(params);
    }

    @ReactMethod
    public void getColorList(ReadableArray StateName, Callback callback) {
        WritableMap params = Arguments.createMap();
        for (int i = 0; i < StateName.size(); i++) {
            String string = StateName.getString(i);
            String path = SkinManager.getInstance().getColorForRN(string);
            LogUtils.e("MainModule", "getColorList path:" + path + "  name:" + string);
            params.putString(string, path);
        }
        callback.invoke(params);
    }
    ......
       
```
##  React Native 通过本地调用换肤 Example

```js

    export default class BaseComponent extends Component {
        componentWillMount() {
            this.nativeChangeThemListener = DeviceEventEmitter.addListener("nativeChangeSkin",
                (params) => this.updateSkin(params));
            BackHandler.addEventListener("hardwareBackPress", this.onBackClicked);
        }

        componentWillUnmount() {
            BackHandler.removeEventListener("hardwareBackPress", this.onBackClicked);
            if (this.nativeChangeThemListener)
                this.nativeChangeThemListener.remove();
        }

        updateSkin(params){

        }
    }


    const SkinModule = NativeModules.SkinModule;
    export default class SkinSetting extends BaseComponent {
        constructor(props) {
            super(props);
            this.state = {
                gift_0:props.gift_0,
                home_0:props.home_0,
                watch_0:props.watch_0,
                primary:props.primary,
                colorPrimary:props.colorPrimary,
            };
        }

        /** 重写componentWillMount 方法一定要加 super.componentWillMount() 方法添加监听器*/
        componentWillMount() {
            this.updateSkin("");
            super.componentWillMount();
        }

        /**重写 updateSkin 在换肤的时候重新获取皮肤资源*/
        updateSkin(params) {
            var colorList = new Array();
            colorList.push("primary");
            colorList.push("colorPrimary");
            var imageList = new Array();
            imageList.push("home_0");
            imageList.push("watch_0");
            imageList.push("gift_0");
            SkinModule.getColorImageList(colorList, imageList, (result) => this.setState(result))
        }
        render() {
            return (
                <View style={{
                    flex: 1,
                    alignItems: 'center',
                    backgroundColor:this.state.primary,

                }}>
                    <TouchableOpacity onPress={() => this.changeSkin1()}>
                        <Text style={{color: this.state.colorPrimary, fontSize: 30}}>changeSkin1</Text>
                    </TouchableOpacity>
                    <View>
                        <Image source={{uri: this.state.gift_0}} style={{width: 100, height: 100}}/>
                        <Image source={{uri: this.state.home_0}} style={{width: 100, height: 100}}/>
                        <Image source={{uri: this.state.watch_0}} style={{width: 100, height: 100}}/>
                    </View>
                </View>
            );
        }
    }

       
```

......
## 注意 皮肤制作的时候，文件名不能带"："
## License

The MIT License
