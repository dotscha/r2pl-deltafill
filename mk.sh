docker run -it --rm --user `id -u`:`id -g` -v `dirname \`pwd\``:/a -w /a/`basename \`pwd\`` adotsch/dev6502 make $@
