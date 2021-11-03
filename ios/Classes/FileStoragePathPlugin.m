#import "FileStoragePathPlugin.h"
#if __has_include(<file_storage_path/file_storage_path-Swift.h>)
#import <file_storage_path/file_storage_path-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "file_storage_path-Swift.h"
#endif

@implementation FileStoragePathPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFileStoragePathPlugin registerWithRegistrar:registrar];
}
@end
