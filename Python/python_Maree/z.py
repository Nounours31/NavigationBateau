class Tmp:
    @staticmethod
    def func1():
        return 1234
    X = Tmp.func1() 

print(Tmp.X)