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
```Java
  PhotoPickerIntent intent = new PhotoPickerIntent(MainActivity.this);
  intent.setSelectModel(SelectModel.MULTI);
  intent.setShowCarema(true);
  startActivityForResult(intent, 999);
```
#### 选择后回调
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
#### api
##### 设置选择模式，单选（SelectModel.SINGLE）还是多选（SelectModel.MULTI）
```Java
void setSelectModel(SelectModel model);
```
##### 设置最大勾选数量，默认为9张图片
```Java
void setMaxTotal(int total);
```
##### 设置是否显示拍照按钮
```Java
void setShowCarema(boolean bool);
```
##### 设置默认选中图片
```Java
void setSelectedPaths(ArrayList<String> imagePathis);
```
##### 设置图片属性
```Java
void setImageConfig(ImageConfig config);
```
