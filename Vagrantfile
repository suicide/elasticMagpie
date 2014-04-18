VAGRANTFILE_API_VERSION = "2"

$script = <<SCRIPT
wget -O - http://packages.elasticsearch.org/GPG-KEY-elasticsearch | sudo apt-key add -
sudo sh -c "echo 'deb http://packages.elasticsearch.org/elasticsearch/1.1/debian stable main' >> /etc/apt/sources.list"
sudo apt-get update
sudo apt-get -y install openjdk-7-jdk
sudo apt-get -y install elasticsearch
sudo /etc/init.d/elasticsearch start
SCRIPT

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = "saucy64"
  config.vm.box_url = "http://cloud-images.ubuntu.com/vagrant/saucy/current/saucy-server-cloudimg-amd64-vagrant-disk1.box"
  config.vm.network :forwarded_port, guest: 9200, host: 9200
  config.vm.network :forwarded_port, guest: 9300, host: 9300
  #config.vm.provision "shell" do |s|
  #  s.inline = "wget -O - http://packages.elasticsearch.org/GPG-KEY-elasticsearch | sudo apt-key add -"
  #  s.inline = "sudo sh -c \"echo 'deb http://packages.elasticsearch.org/elasticsearch/1.1/debian stable main' >> /etc/apt/sources.list\""
  #  s.inline = "sudo apt-get update"
  #  s.inline = "sudo apt-get -y install openjdk-7-jdk"
  #  s.inline = "sudo apt-get -y install elasticsearch"
  #end
  config.vm.provision "shell", inline: $script  
end
