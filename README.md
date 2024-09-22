### Daijishou NAS fix/workaround

See here:
https://github.com/TapiocaFox/Daijishou/issues/385

This app receives an intent from Daijishou, converts a network URL path to a ROM to a local path that RetroArch acceps, and calls RetroArch with the new path. For example, converting this:

`content://com.android.externalstorage.documents/tree/0f13853c-6513-8a4e-d5ca-2af0f76c581e%3AEmu%2FRoms%2FFC/document/0f13853c-6513-8a4e-d5ca-2af0f76c581e%3AEmu%2FRoms%2FFC%2FAdventure%20Island%20(USA).zip`

To this:

`/storage/MyMount/Emu/Roms/FC/Adventure Island (USA).zip`

### How to use:

1) Have your NAS mounted

2) Sideload the APK

3) Navigate to Daijishou settings > Library > Add and inspect > Inspect players and open a player

4) In the "Player am start arguments" field:
    - Replace `com.retroarch/com.retroarch.browser.retroactivity.RetroActivityFuture` by `d.n.f/d.n.f.MA`
    - Replace `{file.path}` by `{file.uri}`
    - Add a `NAS_ROOT` argument: `-e NAS_ROOT <root path of your NAS>`, for example `-e NAS_ROOT /storage/MyMount`. You can find the root using an app like X-plore File Manager to navigate to the mounted folder.

    Example of the final result:
    ```
    -n d.n.f/d.n.f.MA
      -e ROM {file.uri}
      -e NAS_ROOT /storage/MyMount
      -e LIBRETRO /data/data/com.retroarch/cores/fceumm_libretro_android.so
      -e CONFIGFILE /storage/emulated/0/Android/data/com.retroarch/files/retroarch.cfg
      -e IME com.android.inputmethod.latin/.LatinIME
      -e DATADIR /data/data/com.retroarch
      -e APK /data/app/com.retroarch-1/base.apk
      -e SDCARD /storage/emulated/0
      -e EXTERNAL /storage/emulated/0/Android/data/com.retroarch/files
      --activity-clear-task
      --activity-clear-top
    ```

6) Try opening a ROM

If it doesn't work, try adding `-e DNF_DEBUG 1`, and a toast will show when launching a ROM showing the original and converted paths.
