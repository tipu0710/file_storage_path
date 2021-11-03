import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:file_storage_path/file_storage_path.dart';

void main() {
  const MethodChannel channel = MethodChannel('file_storage_path');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    //expect(await FileStoragePath.platformVersion, '42');
  });
}
