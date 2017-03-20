#!/bin/bash

pachage_path="/home/project/dockerdemo"
package_file="package.tgz"
file_name="dockerdemo.jar"

cd $pachage_path
echo "workDir is `pwd`"

#### 解压缩包
if [ -e "$package_file" ]
then
   echo "find package file: $package_file"
   tar -zvxf $package_file
else
   echo "$package_file doesn't exist"
fi

#### 执行jar文件
if [ -e "$file_name" ]
then
   java -jar $file_name
else
   echo "$file_name doesn't exist, exit"
fi


