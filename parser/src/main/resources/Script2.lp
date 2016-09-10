#Group   Country Rank    Jersey  Position        Age     Selections      Club    Player  Captain         
inputFolder('/tmp/input')
line->split(',')=>all
all->element(2)=>country
all->element(6)=>club
[club,country]->store('/tmp/out/wc.csv')
