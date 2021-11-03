import 'dart:async';
import 'package:permission_handler/permission_handler.dart';

import 'package:flutter/services.dart';

enum FileType { all, image, video, audio }

class FileStoragePath {
  static const MethodChannel _channel = MethodChannel('file_storage_path');

  static Future<String?> getFile(FileType fileType) async {
    if (await _getPermission()) {
      try{
        switch (fileType) {
          case FileType.all:
            return await _filePath();
          case FileType.image:
            return await _imagesPath();
          case FileType.video:
            return await _videoPath();
          case FileType.audio:
            return await _audioPath();
          default:
            return null;
        }
      }catch (e){
        rethrow;
      }
    }
  }

  static Future<String?> _imagesPath() async =>
      await _channel.invokeMethod<String>('getImagesPath');

  static Future<String?> _videoPath() async =>
      await _channel.invokeMethod<String>('getVideosPath');

  static Future<String?> _audioPath() async =>
      await _channel.invokeMethod<String>('getAudioPath');

  static Future<String?> _filePath() async =>
      await _channel.invokeMethod<String>('getFilesPath');

  static Future<bool> _getPermission() async {
    return await Permission.storage.request() == PermissionStatus.granted;
  }
}
