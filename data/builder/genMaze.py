import mazeBuilder
import subprocess

def main():
######define maze name########
  name = 'mymaze' 
######define maze name########

  m = mazeBuilder.Maze() #Init. Doun't touch this

###Draw maze as row and col###
###Add as many as you want!###
  m.addRow(0,20)
  m.addCol(0,20)
  m.addRow(40,20)
  m.addCol(20,20)
  m.addCol(10,20)
  m.addRow(30,20)
###Draw maze as row and col###

#####Add Teleporter points####
##Up-3,Right-4,Down-5,Left-6##
###Add as many as you want!###
  #m.linkNodes(0,100,6)
  #For example, this means, 'at Node0', go 'left' will teleport to 'Node50'
  #And the reverse way(Node50 to Node0 is automatically added)
  #m.linkNodes(130,230,6)
#####Add Teleporter points####

##Define density of small pills##
  m.dispersePills(4)
##Define density of small pills##

##Put power Pill at some Node##
####Add as many as you want!###
  m.setPower(50)
##Put power Pill at some Node##

##Set Pacman start Node and Ghost start Node##
  m.setStartNodes(0, 89)
##Set Pacman start Node and Ghost start Node##

##Set Ghost holder as X Y coordinates##
  m.setGhostLair(50,50)
##Set Ghost holder as X Y coordinates##

  m.checkMaze() #Check Map
  #Save and generate distance file.
  m.save(name)
  subprocess.call(["python","calcDistances.py",name+".txt"])


if __name__=="__main__":
  main()



