VAGRANTFILE_API_VERSION = "2"

$script = <<SCRIPT
wget -O - http://packages.elasticsearch.org/GPG-KEY-elasticsearch | sudo apt-key add -
sudo sh -c "echo 'deb http://packages.elasticsearch.org/elasticsearch/1.2/debian stable main' >> /etc/apt/sources.list"
sudo apt-get update
sudo apt-get -y install openjdk-7-jdk
sudo apt-get -y install elasticsearch
sudo /etc/init.d/elasticsearch start
SCRIPT

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = "trusty64"
  config.vm.box_url = "https://cloud-images.ubuntu.com/vagrant/trusty/current/trusty-server-cloudimg-amd64-vagrant-disk1.box"
  config.vm.network :forwarded_port, guest: 9200, host: 9200
  config.vm.network :forwarded_port, guest: 9300, host: 9300
  config.vm.provider "virtualbox" do |v|
    v.memory = 2048
    v.cpus = 2
  end
  config.vm.provision "shell", inline: $script
end
