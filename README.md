## 拍照、图片选择框架
#### 使用方法：
#### 依赖
将其添加到根build.gradle文件（而不是模块build.gradle文件）中：

```Xml
    allprojects {
        repositories {
            maven { url "https://jitpack.io" }
        }
    }
```
然后，将库添加到模块中 build.gradle
```Xml
    dependencies {
        implementation 'com.github.goodgt:PhotoPicker:1.0.0'
    }
```
#### 调用
##### 方式一
```Java
  PhotoPickerIntent intent = new PhotoPickerIntent(MainActivity.this);
  intent.setSelectModel(SelectModel.MULTI);
  intent.setShowCarema(true);
  startActivityForResult(intent, 999);
```
###### 选择后回调
```Java
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
      ArrayList<String> imgPaths = new ArrayList<>();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 999:
                    imgPaths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    for (String str : imgPaths) {
                        Log.e("imgPath", str);
                    }
                    break;
            }
        }
    }
```
##### 方式二
```Java
  PhotoPickerIntent intent = new PhotoPickerIntent(MainActivity.this);
  intent.setSelectModel(SelectModel.MULTI);
  intent.setShowCarema(true);
  intent.gotoPhotoPickerActivity(MainActivity.this, new PhotoPickerActivity.OnSelectedCallbackListener() {
        @Override
                    public void onSelectedCallback(ArrayList<String> resultList) {
                        for (String str : resultList) {
                            Log.e("imgPath", str);
                        }
                    }
                });
```

#### api
##### 设置选择模式，默认单张 单选（SelectModel.SINGLE）还是多选（SelectModel.MULTI）
```Java
void setSelectModel(SelectModel model);
```
##### 设置最大勾选数量，默认为9张图片
```Java
void setMaxTotal(int total);
```
##### 设置是否显示拍照按钮 默认不显示
```Java
void setShowCarema(boolean bool);
```
##### 默认已选择的照片地址（做回显用）
```Java
void setSelectedPaths(ArrayList<String> imagePathis);
```
##### 显示相册图片的属性（详见 ImageConfig）
```Java
void setImageConfig(ImageConfig config);
```
