from MareeScrapperShom import MareeScrapperShom
import os


x : MareeScrapperShom = MareeScrapperShom()
print ("x:" ,x)
loggingInitPath = os.path.join(os.path.dirname(__file__), ".\\odtref\\shom_list_port.odt.xml")
with open(loggingInitPath, "r") as f:
    xml = f.read()
x.getListPort(xml=xml)

loggingInitPath = os.path.join(os.path.dirname(__file__), ".\\odtref\\shom.odt.maregramme.txt")
with open(loggingInitPath, "r") as f:
    xml = f.read()
x.getPortMaree(port="PAIMPOL",dateDebut="20240911", dureeEnJour=14, outputName="maree.shom.csv", xml=xml)