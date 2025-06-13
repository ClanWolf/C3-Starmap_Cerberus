; ClanWolf.net [CWG]
; C3 Client Installer script
; c3-client.nsi
;--------------------------------

!include LogicLib.nsh

Unicode true

Var STR_HAYSTACK
Var STR_NEEDLE
Var STR_CONTAINS_VAR_1
Var STR_CONTAINS_VAR_2
Var STR_CONTAINS_VAR_3
Var STR_CONTAINS_VAR_4
Var STR_RETURN_VAR

; StrContains
; This function does a case sensitive searches for an occurrence of a substring in a string.
; It returns the substring if it is found.
; Otherwise it returns null("").
; Written by kenglish_hi
; Adapted from StrReplace written by dandaman32
Function StrContains
  Exch $STR_NEEDLE
  Exch 1
  Exch $STR_HAYSTACK
  ; Uncomment to debug
  ;MessageBox MB_OK 'STR_NEEDLE = $STR_NEEDLE STR_HAYSTACK = $STR_HAYSTACK '
    StrCpy $STR_RETURN_VAR ""
    StrCpy $STR_CONTAINS_VAR_1 -1
    StrLen $STR_CONTAINS_VAR_2 $STR_NEEDLE
    StrLen $STR_CONTAINS_VAR_4 $STR_HAYSTACK
    loop:
      IntOp $STR_CONTAINS_VAR_1 $STR_CONTAINS_VAR_1 + 1
      StrCpy $STR_CONTAINS_VAR_3 $STR_HAYSTACK $STR_CONTAINS_VAR_2 $STR_CONTAINS_VAR_1
      StrCmp $STR_CONTAINS_VAR_3 $STR_NEEDLE found
      StrCmp $STR_CONTAINS_VAR_1 $STR_CONTAINS_VAR_4 done
      Goto loop
    found:
      StrCpy $STR_RETURN_VAR $STR_NEEDLE
      Goto done
    done:
   Pop $STR_NEEDLE ;Prevent "invalid opcode" errors and keep the
   Exch $STR_RETURN_VAR
FunctionEnd

!macro _StrContainsConstructor OUT NEEDLE HAYSTACK
  Push `${HAYSTACK}`
  Push `${NEEDLE}`
  Call StrContains
  Pop `${OUT}`
!macroend

!define StrContains '!insertmacro "_StrContainsConstructor"'

; ------------------------------------------------------------------------------

Name "C3-Client_Installer"
Caption "C3 Client Installer"
Icon "c3.ico"
UninstallIcon "c3.ico"
OutFile "C3-Client-7.5.18_install.exe"
BrandingText /TRIMRIGHT "ClanWolf.net"

InstallDir $PROGRAMFILES64\C3-Client
InstallDirRegKey HKLM "Software\C3-Client" "Install_Dir"
RequestExecutionLevel admin
; RequestExecutionLevel user
AddBrandingImage left 130
SetFont /LANG=${LANG_ENGLISH} "Arial" 9

; start shortcut at the end of the installation
Function .onInstSuccess
    ;MessageBox MB_OK "Reached LaunchLink $\r$\n \
    ;                 SMPROGRAMS: $SMPROGRAMS  $\r$\n \
    ;                 Start Menu Folder: $STARTMENU_FOLDER $\r$\n \
    ;                 InstallDirectory: $INSTDIR "
    ExecShell "open" "https://www.clanwolf.net/apps/C3/changelog.txt?refresh=true&r=1749855133829"
    ExecShell "open" "$INSTDIR\bin\C3-Starmap_Cerberus_noWin.cmd" "" SW_HIDE
FunctionEnd

; uninstall before install
; https://nsis.sourceforge.io/mediawiki/index.php?title=Auto-uninstall_old_before_installing_new&oldid=24968
Function .onInit
	ReadRegStr $R0 HKLM \
	"Software\Microsoft\Windows\CurrentVersion\Uninstall\C3-Client" \
	"UninstallString"
	StrCmp $R0 "" done

	MessageBox MB_OKCANCEL|MB_USERICON \
	"C3-Client is already installed. $\n$\nClick `OK` to remove the \
	previous version or `Cancel` to cancel this upgrade." \
	IDOK uninst
	Abort

	;Run the uninstaller
	uninst:
		ClearErrors
		ExecWait '$R0 _?=$INSTDIR'
		IfErrors 0 noError
			GetErrorLevel $0 ;check the return error value from the uninstaller
			MessageBox MB_USERICON|MB_OK "Uninstall aborted ($0).$\n$\n\
			Old version needs to be removed first."
			Abort
		noError:
	done:
FunctionEnd

; installer image
Function .onGuiInit
	InitPluginsDir
	File "/oname=$PluginsDir\inst.bmp" "inst.bmp"
	SetBrandingImage /resizetofit "$PluginsDir\inst.bmp"
FunctionEnd

; uninstaller image
Function un.onGuiInit
	InitPluginsDir
	File "/oname=$PluginsDir\uninst.bmp" "uninst.bmp"
	SetBrandingImage /resizetofit "$PluginsDir\uninst.bmp"
FunctionEnd

; Needs !include LogicLib.nsh
; Fires every time the instdir var is changed
Function .onVerifyInstDir
    ${If} ${FileExists} `$INSTDIR\*.*`
        ; file is a directory
        ;MessageBox MB_OK "Install directory already exists!"
    ${ElseIf} ${FileExists} `$INSTDIR\`
        ; file is a file
        ;MessageBox MB_OK "Install directory name does already exist as a file!"
    ${Else}
        ; file is neither a file or a directory (i.e. it doesn't exist)
        ;MessageBox MB_OK "Install directory does not exist, will be created!"
    ${EndIf}
FunctionEnd

; Fires if instdir selection is finished (by clicking next)
; https://nsis-dev.github.io/NSIS-Forums/html/t-196015.html
Function DirectoryLeave
	${StrContains} $0 $PROGRAMFILES64 $INSTDIR
	StrCmp $0 "" notfound
		;MessageBox MB_OK 'Path seems to be ok!'
		;Quit
		Goto done
	notfound:
        MessageBox MB_OK 'This will install the C3-Client outside the Programs folder!$\nRecommended folder: $PROGRAMFILES64\C3-Client$\n$\nProceed at your own risk!'
        ;Quit
	done:
FunctionEnd

; DSGVO request
;Function DSGVO
;  MessageBox MB_YESNO "DSGVO:$\nDo you agree to save and process account data?$\n$\nRefer to the manual for details!" IDYES true IDNO false
;  true:
;    Goto next
;  false:
;    Quit
;  next:
;FunctionEnd

;--------------------------------

LoadLanguageFile "${NSISDIR}\Contrib\Language files\English.nlf"
VIProductVersion "7.5.18.0"
VIAddVersionKey /LANG=0 "ProductName" "C3 Client"
VIAddVersionKey /LANG=0 "Comments" "StarMap"
VIAddVersionKey /LANG=0 "CompanyName" "ClanWolf.net [CWG]"
VIAddVersionKey /LANG=0 "LegalTrademarks" "StarMap of the Inner Sphere and Clan Space."
VIAddVersionKey /LANG=0 "LegalCopyright" "© ClanWolf.net"
VIAddVersionKey /LANG=0 "FileDescription" "StarMap"
VIAddVersionKey /LANG=0 "FileVersion" "7.5.18"

;--------------------------------

;Page license
PageEx license
  LicenseData ../LICENSE
  LicenseForceSelection checkbox
PageExEnd

;PageEx
;  LicenseText "Changelog"
;  LicenseData ../doc/changelog.txt
;PageExEnd

;Page Custom DSGVO
Page components
Page directory "" "" "DirectoryLeave"
Page instfiles

UninstPage uninstConfirm
UninstPage instfiles

;--------------------------------

; The files to install
Section "C3-Client (required)"
    SectionIn RO
	SetShellVarContext all
	SetOutPath $INSTDIR
	File /r "c3.ico"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\release"

	CreateDirectory $INSTDIR\lib
	SetOutpath $INSTDIR\lib
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\lib\tzmappings"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\lib\tzdb.dat"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\lib\psfontj2d.properties"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\lib\psfont.properties.ja"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\lib\modules"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\lib\jvm.lib"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\lib\jvm.cfg"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\lib\jrt-fs.jar"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\lib\jawt.lib"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\lib\fontconfig.properties.src"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\lib\fontconfig.bfc"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\lib\classlist"

	CreateDirectory $INSTDIR\lib\security
	SetOutpath $INSTDIR\lib\security
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\lib\security\public_suffix_list.dat"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\lib\security\cacerts"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\lib\security\blocked.certs"

	CreateDirectory $INSTDIR\legal
	SetOutpath $INSTDIR\legal

	CreateDirectory $INSTDIR\legal\jdk.xml.dom
	SetOutpath $INSTDIR\legal\jdk.xml.dom
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.xml.dom\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.xml.dom\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.xml.dom\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\jdk.unsupported.desktop
	SetOutpath $INSTDIR\legal\jdk.unsupported.desktop
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.unsupported.desktop\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.unsupported.desktop\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.unsupported.desktop\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\jdk.unsupported
	SetOutpath $INSTDIR\legal\jdk.unsupported
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.unsupported\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.unsupported\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.unsupported\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\jdk.jsobject
	SetOutpath $INSTDIR\legal\jdk.jsobject
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.jsobject\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.jsobject\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.jsobject\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\jdk.crypto.cryptoki
	SetOutpath $INSTDIR\legal\jdk.crypto.cryptoki
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.crypto.cryptoki\pkcs11wrapper.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.crypto.cryptoki\pkcs11cryptotoken.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.crypto.cryptoki\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.crypto.cryptoki\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\jdk.crypto.cryptoki\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\java.xml
	SetOutpath $INSTDIR\legal\java.xml
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\xmlxsd.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\xmlspec.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\xhtml11schema.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\xhtml11.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\xhtml10schema.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\xhtml10.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\xerces.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\xalan.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\schema10part2.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\schema10part1.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\jcup.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\dom.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\bcel.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.xml\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\java.transaction.xa
	SetOutpath $INSTDIR\legal\java.transaction.xa
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.transaction.xa\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.transaction.xa\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.transaction.xa\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\java.sql
	SetOutpath $INSTDIR\legal\java.sql
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.sql\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.sql\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.sql\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\java.scripting
	SetOutpath $INSTDIR\legal\java.scripting
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.scripting\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.scripting\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.scripting\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\java.prefs
	SetOutpath $INSTDIR\legal\java.prefs
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.prefs\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.prefs\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.prefs\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\java.net.http
	SetOutpath $INSTDIR\legal\java.net.http
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.net.http\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.net.http\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.net.http\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\java.management
	SetOutpath $INSTDIR\legal\java.management
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.management\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.management\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.management\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\java.logging
	SetOutpath $INSTDIR\legal\java.logging
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.logging\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.logging\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.logging\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\java.instrument
	SetOutpath $INSTDIR\legal\java.instrument
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.instrument\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.instrument\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.instrument\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\java.desktop
	SetOutpath $INSTDIR\legal\java.desktop
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.desktop\mesa3d.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.desktop\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.desktop\libpng.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.desktop\lcms.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.desktop\jpeg.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.desktop\harfbuzz.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.desktop\giflib.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.desktop\freetype.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.desktop\colorimaging.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.desktop\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.desktop\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\java.datatransfer
	SetOutpath $INSTDIR\legal\java.datatransfer
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.datatransfer\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.datatransfer\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.datatransfer\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\legal\java.base
	SetOutpath $INSTDIR\legal\java.base
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.base\zlib.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.base\wepoll.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.base\unicode.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.base\siphash.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.base\public_suffix.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.base\LICENSE"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.base\icu.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.base\cldr.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.base\c-libutl.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.base\ASSEMBLY_EXCEPTION"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.base\asm.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.base\aes.md"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\legal\java.base\ADDITIONAL_LICENSE_INFO"

	CreateDirectory $INSTDIR\jars
	SetOutpath $INSTDIR\jars

	CreateDirectory $INSTDIR\include
	SetOutpath $INSTDIR\include
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\include\jvmticmlr.h"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\include\jvmti.h"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\include\jni.h"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\include\jawt.h"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\include\classfile_constants.h"

	CreateDirectory $INSTDIR\include\win32
	SetOutpath $INSTDIR\include\win32
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\include\win32\jni_md.h"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\include\win32\jawt_md.h"

	CreateDirectory $INSTDIR\conf
	SetOutpath $INSTDIR\conf
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\conf\sound.properties"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\conf\net.properties"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\conf\logging.properties"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\conf\jaxp.properties"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\conf\jaxp-strict.properties.template"

	CreateDirectory $INSTDIR\conf\security
	SetOutpath $INSTDIR\conf\security
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\conf\security\java.security"

	CreateDirectory $INSTDIR\conf\security\policy
	SetOutpath $INSTDIR\conf\security\policy
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\conf\security\policy\README.txt"

	CreateDirectory $INSTDIR\conf\security\policy\unlimited
	SetOutpath $INSTDIR\conf\security\policy\unlimited
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\conf\security\policy\unlimited\default_US_export.policy"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\conf\security\policy\unlimited\default_local.policy"

	CreateDirectory $INSTDIR\conf\security\policy\limited
	SetOutpath $INSTDIR\conf\security\policy\limited
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\conf\security\policy\limited\exempt_local.policy"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\conf\security\policy\limited\default_US_export.policy"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\conf\security\policy\limited\default_local.policy"

	CreateDirectory $INSTDIR\bin
	SetOutpath $INSTDIR\bin
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\zip.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\verify.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\vcruntime140_1.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\vcruntime140.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\ucrtbase.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\syslookup.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\splashscreen.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\prefs.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\nio.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\net.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\msvcp140.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\mlib_image.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\management.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\lcms.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\keytool.exe"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\jsound.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\jrunscript.exe"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\jli.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\jimage.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\jawt.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\javaw.exe"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\javajpeg.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\java.exe"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\java.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\j2pkcs11.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\instrument.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\freetype.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\fontmanager.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\C3-Starmap_Cerberus_win.lnk"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\C3-Starmap_Cerberus_win.cmd"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\C3-Starmap_Cerberus_noWin.lnk"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\C3-Starmap_Cerberus_noWin.cmd"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\C3-Starmap_Cerberus.bat"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\C3-Starmap_Cerberus"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\awt.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-crt-utility-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-crt-time-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-crt-string-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-crt-stdio-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-crt-runtime-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-crt-process-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-crt-private-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-crt-multibyte-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-crt-math-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-crt-locale-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-crt-heap-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-crt-filesystem-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-crt-environment-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-crt-convert-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-crt-conio-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-util-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-timezone-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-sysinfo-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-synch-l1-2-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-synch-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-string-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-rtlsupport-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-profile-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-processthreads-l1-1-1.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-processthreads-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-processenvironment-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-namedpipe-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-memory-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-localization-l1-2-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-libraryloader-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-interlocked-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-heap-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-handle-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-file-l2-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-file-l1-2-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-file-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-fibers-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-errorhandling-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-debug-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-datetime-l1-1-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-console-l1-2-0.dll"
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\api-ms-win-core-console-l1-1-0.dll"

	CreateDirectory $INSTDIR\bin\server
	SetOutpath $INSTDIR\bin\server
	File /r "..\net.clanwolf.starmap.client\target\jlink-image\bin\server\jvm.dll"
###FILELIST###

	; Write the installation path into the registry
	WriteRegStr HKLM SOFTWARE\C3-Client "Install_Dir" "$INSTDIR"

	; Write the uninstall keys for Windows
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\C3-Client" "DisplayName" "C3-Client"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\C3-Client" "DisplayIcon" "$INSTDIR\c3.ico"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\C3-Client" "DisplayVersion" "7.5.18"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\C3-Client" "Publisher" "ClanWolf.net [CWG], Christian Bartel"
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

	CreateShortcut "$SMPROGRAMS\C3-Client\C3-Client.lnk" "$INSTDIR\bin\C3-Starmap_Cerberus_noWin.cmd" "" "$INSTDIR\c3.ico" 0 SW_SHOWMINIMIZED
	CreateShortcut "$SMPROGRAMS\C3-Client\C3-Client (Console).lnk" "$INSTDIR\bin\C3-Starmap_Cerberus_win.cmd" "" "$INSTDIR\c3.ico" 0 SW_SHOWNORMAL
	CreateShortcut "$SMPROGRAMS\C3-Client\Changelog.lnk" "https://www.clanwolf.net/apps/C3/changelog.txt?refresh=true&r=1749855133921" "" "" 0
	; CreateShortCut "$SMPROGRAMS\C3-Client\Remove.lnk" "$INSTDIR\Uninstall.exe" "" "$INSTDIR\c3.ico" 0
SectionEnd

;--------------------------------
; Uninstaller

Section "Uninstall"
	SetShellVarContext all

	; Remove registry keys
	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\C3-Client"
	DeleteRegKey HKLM SOFTWARE\C3-Client

	Delete $INSTDIR\release
	Delete $INSTDIR\lib\tzmappings
	Delete $INSTDIR\lib\tzdb.dat
	Delete $INSTDIR\lib\psfontj2d.properties
	Delete $INSTDIR\lib\psfont.properties.ja
	Delete $INSTDIR\lib\modules
	Delete $INSTDIR\lib\jvm.lib
	Delete $INSTDIR\lib\jvm.cfg
	Delete $INSTDIR\lib\jrt-fs.jar
	Delete $INSTDIR\lib\jawt.lib
	Delete $INSTDIR\lib\fontconfig.properties.src
	Delete $INSTDIR\lib\fontconfig.bfc
	Delete $INSTDIR\lib\classlist
	Delete $INSTDIR\lib\security\public_suffix_list.dat
	Delete $INSTDIR\lib\security\cacerts
	Delete $INSTDIR\lib\security\blocked.certs
	Delete $INSTDIR\legal\jdk.xml.dom\LICENSE
	Delete $INSTDIR\legal\jdk.xml.dom\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\jdk.xml.dom\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\jdk.unsupported.desktop\LICENSE
	Delete $INSTDIR\legal\jdk.unsupported.desktop\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\jdk.unsupported.desktop\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\jdk.unsupported\LICENSE
	Delete $INSTDIR\legal\jdk.unsupported\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\jdk.unsupported\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\jdk.jsobject\LICENSE
	Delete $INSTDIR\legal\jdk.jsobject\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\jdk.jsobject\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\jdk.crypto.cryptoki\pkcs11wrapper.md
	Delete $INSTDIR\legal\jdk.crypto.cryptoki\pkcs11cryptotoken.md
	Delete $INSTDIR\legal\jdk.crypto.cryptoki\LICENSE
	Delete $INSTDIR\legal\jdk.crypto.cryptoki\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\jdk.crypto.cryptoki\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\java.xml\xmlxsd.md
	Delete $INSTDIR\legal\java.xml\xmlspec.md
	Delete $INSTDIR\legal\java.xml\xhtml11schema.md
	Delete $INSTDIR\legal\java.xml\xhtml11.md
	Delete $INSTDIR\legal\java.xml\xhtml10schema.md
	Delete $INSTDIR\legal\java.xml\xhtml10.md
	Delete $INSTDIR\legal\java.xml\xerces.md
	Delete $INSTDIR\legal\java.xml\xalan.md
	Delete $INSTDIR\legal\java.xml\schema10part2.md
	Delete $INSTDIR\legal\java.xml\schema10part1.md
	Delete $INSTDIR\legal\java.xml\LICENSE
	Delete $INSTDIR\legal\java.xml\jcup.md
	Delete $INSTDIR\legal\java.xml\dom.md
	Delete $INSTDIR\legal\java.xml\bcel.md
	Delete $INSTDIR\legal\java.xml\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\java.xml\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\java.transaction.xa\LICENSE
	Delete $INSTDIR\legal\java.transaction.xa\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\java.transaction.xa\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\java.sql\LICENSE
	Delete $INSTDIR\legal\java.sql\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\java.sql\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\java.scripting\LICENSE
	Delete $INSTDIR\legal\java.scripting\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\java.scripting\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\java.prefs\LICENSE
	Delete $INSTDIR\legal\java.prefs\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\java.prefs\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\java.net.http\LICENSE
	Delete $INSTDIR\legal\java.net.http\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\java.net.http\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\java.management\LICENSE
	Delete $INSTDIR\legal\java.management\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\java.management\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\java.logging\LICENSE
	Delete $INSTDIR\legal\java.logging\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\java.logging\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\java.instrument\LICENSE
	Delete $INSTDIR\legal\java.instrument\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\java.instrument\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\java.desktop\mesa3d.md
	Delete $INSTDIR\legal\java.desktop\LICENSE
	Delete $INSTDIR\legal\java.desktop\libpng.md
	Delete $INSTDIR\legal\java.desktop\lcms.md
	Delete $INSTDIR\legal\java.desktop\jpeg.md
	Delete $INSTDIR\legal\java.desktop\harfbuzz.md
	Delete $INSTDIR\legal\java.desktop\giflib.md
	Delete $INSTDIR\legal\java.desktop\freetype.md
	Delete $INSTDIR\legal\java.desktop\colorimaging.md
	Delete $INSTDIR\legal\java.desktop\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\java.desktop\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\java.datatransfer\LICENSE
	Delete $INSTDIR\legal\java.datatransfer\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\java.datatransfer\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\legal\java.base\zlib.md
	Delete $INSTDIR\legal\java.base\wepoll.md
	Delete $INSTDIR\legal\java.base\unicode.md
	Delete $INSTDIR\legal\java.base\siphash.md
	Delete $INSTDIR\legal\java.base\public_suffix.md
	Delete $INSTDIR\legal\java.base\LICENSE
	Delete $INSTDIR\legal\java.base\icu.md
	Delete $INSTDIR\legal\java.base\cldr.md
	Delete $INSTDIR\legal\java.base\c-libutl.md
	Delete $INSTDIR\legal\java.base\ASSEMBLY_EXCEPTION
	Delete $INSTDIR\legal\java.base\asm.md
	Delete $INSTDIR\legal\java.base\aes.md
	Delete $INSTDIR\legal\java.base\ADDITIONAL_LICENSE_INFO
	Delete $INSTDIR\include\jvmticmlr.h
	Delete $INSTDIR\include\jvmti.h
	Delete $INSTDIR\include\jni.h
	Delete $INSTDIR\include\jawt.h
	Delete $INSTDIR\include\classfile_constants.h
	Delete $INSTDIR\include\win32\jni_md.h
	Delete $INSTDIR\include\win32\jawt_md.h
	Delete $INSTDIR\conf\sound.properties
	Delete $INSTDIR\conf\net.properties
	Delete $INSTDIR\conf\logging.properties
	Delete $INSTDIR\conf\jaxp.properties
	Delete $INSTDIR\conf\jaxp-strict.properties.template
	Delete $INSTDIR\conf\security\java.security
	Delete $INSTDIR\conf\security\policy\README.txt
	Delete $INSTDIR\conf\security\policy\unlimited\default_US_export.policy
	Delete $INSTDIR\conf\security\policy\unlimited\default_local.policy
	Delete $INSTDIR\conf\security\policy\limited\exempt_local.policy
	Delete $INSTDIR\conf\security\policy\limited\default_US_export.policy
	Delete $INSTDIR\conf\security\policy\limited\default_local.policy
	Delete $INSTDIR\bin\zip.dll
	Delete $INSTDIR\bin\verify.dll
	Delete $INSTDIR\bin\vcruntime140_1.dll
	Delete $INSTDIR\bin\vcruntime140.dll
	Delete $INSTDIR\bin\ucrtbase.dll
	Delete $INSTDIR\bin\syslookup.dll
	Delete $INSTDIR\bin\splashscreen.dll
	Delete $INSTDIR\bin\prefs.dll
	Delete $INSTDIR\bin\nio.dll
	Delete $INSTDIR\bin\net.dll
	Delete $INSTDIR\bin\msvcp140.dll
	Delete $INSTDIR\bin\mlib_image.dll
	Delete $INSTDIR\bin\management.dll
	Delete $INSTDIR\bin\lcms.dll
	Delete $INSTDIR\bin\keytool.exe
	Delete $INSTDIR\bin\jsound.dll
	Delete $INSTDIR\bin\jrunscript.exe
	Delete $INSTDIR\bin\jli.dll
	Delete $INSTDIR\bin\jimage.dll
	Delete $INSTDIR\bin\jawt.dll
	Delete $INSTDIR\bin\javaw.exe
	Delete $INSTDIR\bin\javajpeg.dll
	Delete $INSTDIR\bin\java.exe
	Delete $INSTDIR\bin\java.dll
	Delete $INSTDIR\bin\j2pkcs11.dll
	Delete $INSTDIR\bin\instrument.dll
	Delete $INSTDIR\bin\freetype.dll
	Delete $INSTDIR\bin\fontmanager.dll
	Delete $INSTDIR\bin\C3-Starmap_Cerberus_win.lnk
	Delete $INSTDIR\bin\C3-Starmap_Cerberus_win.cmd
	Delete $INSTDIR\bin\C3-Starmap_Cerberus_noWin.lnk
	Delete $INSTDIR\bin\C3-Starmap_Cerberus_noWin.cmd
	Delete $INSTDIR\bin\C3-Starmap_Cerberus.bat
	Delete $INSTDIR\bin\C3-Starmap_Cerberus
	Delete $INSTDIR\bin\awt.dll
	Delete $INSTDIR\bin\api-ms-win-crt-utility-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-crt-time-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-crt-string-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-crt-stdio-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-crt-runtime-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-crt-process-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-crt-private-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-crt-multibyte-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-crt-math-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-crt-locale-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-crt-heap-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-crt-filesystem-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-crt-environment-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-crt-convert-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-crt-conio-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-util-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-timezone-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-sysinfo-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-synch-l1-2-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-synch-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-string-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-rtlsupport-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-profile-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-processthreads-l1-1-1.dll
	Delete $INSTDIR\bin\api-ms-win-core-processthreads-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-processenvironment-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-namedpipe-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-memory-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-localization-l1-2-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-libraryloader-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-interlocked-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-heap-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-handle-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-file-l2-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-file-l1-2-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-file-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-fibers-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-errorhandling-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-debug-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-datetime-l1-1-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-console-l1-2-0.dll
	Delete $INSTDIR\bin\api-ms-win-core-console-l1-1-0.dll
	Delete $INSTDIR\bin\server\jvm.dll

	Delete "$INSTDIR\c3.ico"
	Delete "$INSTDIR\uninstall.exe"
	Delete "$INSTDIR\bin\C3-Starmap_Cerberus"
	Delete "$INSTDIR\bin\C3-Starmap_Cerberus.lnk"
	Delete "$SMPROGRAMS\C3-Client\C3-Client.lnk"
	Delete "$SMPROGRAMS\C3-Client\C3-Client (Console).lnk"
	Delete "$SMPROGRAMS\C3-Client\Changelog.lnk"
	; Delete "$SMPROGRAMS\C3-Client\Remove.lnk"

	RMDir "$INSTDIR\bin\server"
	RMDir "$INSTDIR\bin"
	RMDir "$INSTDIR\conf\security\policy\limited"
	RMDir "$INSTDIR\conf\security\policy\unlimited"
	RMDir "$INSTDIR\conf\security\policy"
	RMDir "$INSTDIR\conf\security"
	RMDir "$INSTDIR\conf"
	RMDir "$INSTDIR\include\win32"
	RMDir "$INSTDIR\include"
	RMDir "$INSTDIR\jars"
	RMDir "$INSTDIR\legal\java.base"
	RMDir "$INSTDIR\legal\java.datatransfer"
	RMDir "$INSTDIR\legal\java.desktop"
	RMDir "$INSTDIR\legal\java.instrument"
	RMDir "$INSTDIR\legal\java.logging"
	RMDir "$INSTDIR\legal\java.management"
	RMDir "$INSTDIR\legal\java.net.http"
	RMDir "$INSTDIR\legal\java.prefs"
	RMDir "$INSTDIR\legal\java.scripting"
	RMDir "$INSTDIR\legal\java.sql"
	RMDir "$INSTDIR\legal\java.transaction.xa"
	RMDir "$INSTDIR\legal\java.xml"
	RMDir "$INSTDIR\legal\jdk.crypto.cryptoki"
	RMDir "$INSTDIR\legal\jdk.jsobject"
	RMDir "$INSTDIR\legal\jdk.unsupported"
	RMDir "$INSTDIR\legal\jdk.unsupported.desktop"
	RMDir "$INSTDIR\legal\jdk.xml.dom"
	RMDir "$INSTDIR\legal"
	RMDir "$INSTDIR\lib\security"
	RMDir "$INSTDIR\lib"

	SetOutPath "$PROGRAMFILES"
	Delete "$INSTDIR\uninstall.exe"

	; Do NOT use 'RMDir /r "$INSTDIR"' (recursive)!
	; User might have installed in a wrong folder or
	; var "$INSTDIR" might have been wiped clean.
	; Resulting in the wrong folder being deleted!
	RMDir "$INSTDIR"
	RMDir "$SMPROGRAMS\C3-Client"

SectionEnd
