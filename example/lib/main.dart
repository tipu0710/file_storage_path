import 'dart:convert';
import 'dart:io';
import 'package:file_storage_path/file_storage_path.dart';
import 'package:flutter/material.dart';
import 'dart:async';
import 'package:flutter/services.dart';

import 'file_model.dart';

void main() => runApp(const MyApp());

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String imagePath = "";
  @override
  void initState() {
    super.initState();
    getAudioPath();
    getVideoPath();
  }

  Future<String?> getImagesPath() async {
    String? imagespath;
    try {
      imagespath = await (FileStoragePath.getFile(FileType.image) as FutureOr<String>);
      var response = jsonDecode(imagespath);
      var imageList = response as List;
      List<FileModel> list =
      imageList.map<FileModel>((json) => FileModel.fromJson(json)).toList();

      setState(() {
        imagePath = list[11].files![0];
      });
    } on PlatformException {
      imagespath = 'Failed to get path';
    }
    return imagespath;
  }

  Future<String?> getVideoPath() async {
    String? videoPath;
    try {
      videoPath = await (FileStoragePath.getFile(FileType.video) as FutureOr<String>);
      var response = jsonDecode(videoPath);
      print(response);
    } on PlatformException {
      videoPath = 'Failed to get path';
    }
    return videoPath;
  }

  Future<String?> getAudioPath() async {
    String? audioPath = "";
    try {
      audioPath = await (FileStoragePath.getFile(FileType.audio) as FutureOr<String>);
      var response = jsonDecode(audioPath);
      print(response);
    } on PlatformException {
      audioPath = 'Failed to get path';
    }
    return audioPath;
  }

  Future<String?> getFilePath() async {
    String? filePath;
    try {
      filePath = await (FileStoragePath.getFile(FileType.all) as FutureOr<String>);
      var response = jsonDecode(filePath);
      print(response);
    } on PlatformException {
      filePath = 'Failed to get path';
    }
    return filePath;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: SizedBox(
            width: 200,
            height: 200,
            child: imagePath != ""
                ? Image.file(
              File(imagePath),
              fit: BoxFit.contain,
            )
                : Container(),
          ),
        ),
      ),
    );
  }
}
