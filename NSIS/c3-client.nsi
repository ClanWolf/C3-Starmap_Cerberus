; ClanWolf.net [CWG]
; C3 Client Installer script
; c3-client.nsi
;--------------------------------

Name "C3-Client_Installer"
Caption "C3 Client Installer"
Icon "c3.ico"
OutFile "C3-Client-5.1.7_install.exe"                                                                                   ; --- replace version ---
BrandingText /TRIMRIGHT "ClanWolf.net"

InstallDir $PROGRAMFILES64\C3-Client
InstallDirRegKey HKLM "Software\C3-Client" "Install_Dir"
RequestExecutionLevel admin

;--------------------------------

LoadLanguageFile "${NSISDIR}\Contrib\Language files\English.nlf"
VIProductVersion "5.1.7.0"                                                                                              ; --- replace version ---
VIAddVersionKey /LANG=0 "ProductName" "C3 Client"
VIAddVersionKey /LANG=0 "Comments" "StarMap"
VIAddVersionKey /LANG=0 "CompanyName" "ClanWolf.net [CWG]"
VIAddVersionKey /LANG=0 "LegalTrademarks" "StarMap of the Inner Sphere and Clan Space."
VIAddVersionKey /LANG=0 "LegalCopyright" "© ClanWolf.net"
VIAddVersionKey /LANG=0 "FileDescription" "StarMap"
VIAddVersionKey /LANG=0 "FileVersion" "5.1.7"                                                                           ; --- replace version ---

;--------------------------------

Page components
Page directory
Page instfiles

UninstPage uninstConfirm
UninstPage instfiles

;--------------------------------

; The files to install
Section "C3-Client (required)"

    SectionIn RO
	SetShellVarContext all

	SetOutPath $INSTDIR

	!tempfile filelist
	!system '"list_contents.cmd" "C:\C3\projects\C3-Starmap_Cerberus\net.clanwolf.starmap.client\target\jlink-image" "${filelist}"'
	!include "${filelist}"
	!delfile "${filelist}"

	; Write the installation path into the registry
	WriteRegStr HKLM SOFTWARE\C3-Client "Install_Dir" "$INSTDIR"

	; Write the uninstall keys for Windows
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\C3-Client" "DisplayName" "C3-Client"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\C3-Client" "DisplayIcon" "$INSTDIR\run.exe"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\C3-Client" "DisplayVersion" "5.1.7"           ; --- replace version ---
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\C3-Client" "Publisher" "ClanWolf.net [CWG]"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\C3-Client" "InstallSource" "$EXEDIR\"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\C3-Client" "UninstallString" '"$INSTDIR\uninstall.exe"'
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\C3-Client" "NoModify" 1
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\C3-Client" "NoRepair" 1
	WriteUninstaller "uninstall.exe"

SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts"

	SetShellVarContext all

	SetOutpath $INSTDIR ; this folder is used as working directory for the following links
	CreateDirectory "$SMPROGRAMS\C3-Client"
	CreateShortcut "$SMPROGRAMS\C3-Client\C3-Client.lnk" "$INSTDIR\run.exe" "" "$INSTDIR\run.exe" 0
	CreateShortcut "$SMPROGRAMS\C3-Client\C3-Client (Debug).lnk" "$INSTDIR\run_debug.exe" "" "$INSTDIR\run_debug.exe" 0

SectionEnd

;--------------------------------

; Uninstaller

Section "Uninstall"

	SetShellVarContext all

	; Remove registry keys
	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\C3-Client"
	DeleteRegKey HKLM SOFTWARE\C3-Client

	; Remove files and uninstaller
	Delete $INSTDIR\lib\activation-1.1.jar
	Delete $INSTDIR\lib\C3-Preloader-1.0.0.jar
	Delete $INSTDIR\lib\commons-codec-1.11.jar
	Delete $INSTDIR\lib\commons-collections4-4.1.jar
	Delete $INSTDIR\lib\commons-net-3.6.jar
	Delete $INSTDIR\lib\jackson-annotations-2.9.0.jar
	Delete $INSTDIR\lib\jackson-core-2.9.6.jar
	Delete $INSTDIR\lib\jackson-databind-2.9.6.jar
	Delete $INSTDIR\lib\jackson-jsog-1.1.1.jar
	Delete $INSTDIR\lib\mail-1.4.7.jar
	Delete $INSTDIR\lib\nadclient-0.8-SNAPSHOT.jar
	Delete $INSTDIR\lib\netty-all-4.0.10.Final.jar
	Delete $INSTDIR\lib\tektosyne-6.2.0.jar

	Delete $INSTDIR\C3-Client_Phoenix-4.2.3-jfx.jar                                                                     ; --- replace version ---
	Delete $INSTDIR\run.exe
	Delete $INSTDIR\run_debug.exe
	Delete $INSTDIR\uninstall.exe

	Delete "$SMPROGRAMS\C3-Client\C3-Client.lnk"
	Delete "$SMPROGRAMS\C3-Client\C3-Client (Debug).lnk"

	; Remove Startmenu folder and directories
	RMDir "$INSTDIR\lib"
	RMDir "$INSTDIR"
	RMDir "$SMPROGRAMS\C3-Client"

SectionEnd
