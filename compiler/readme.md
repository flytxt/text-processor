   On Mac
   
   curl https://raw.githubusercontent.com/cloudius-systems/capstan/master/scripts/download | bash
  557  brew install qemu
  594  export CAPSTAN_QEMU_PATH=/usr/local/Cellar/qemu/2.3.0/bin/qemu-system-x86_64 
  597  ls
  598  ~/bin/capstan build -p vbox
  599  ls
  601  ~/bin/capstan i
  602  ~/bin/capstan run compiler
  
  609  ~/bin/capstan run -n bridge -b awdl0 -v  compiler (not working)
