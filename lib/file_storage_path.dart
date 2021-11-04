import 'dart:async';

import 'package:flutter/services.dart';

enum FileType { all, image, video, audio }

class FileStoragePath {
  static const MethodChannel _channel = MethodChannel('file_storage_path');

  static Future<String?> getFile(FileType fileType) async {
    try {
      switch (fileType) {
        case FileType.all:
          return await _filePath;
        case FileType.image:
          return await _imagesPath;
        case FileType.video:
          return await _videoPath;
        case FileType.audio:
          return await _audioPath;
        default:
          return null;
      }
    } catch (e) {
      rethrow;
    }
  }

  static Future<String?> get _imagesPath =>
      _channel.invokeMethod<String>('getImagesPath');

  static Future<String?> get _videoPath =>
      _channel.invokeMethod<String>('getVideosPath');

  static Future<String?> get _audioPath =>
      _channel.invokeMethod<String>('getAudioPath');

  static Future<String?> get _filePath =>
      _channel.invokeMethod<String>('getFilesPath');
}
