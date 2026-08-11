import os

def rename():
    for f in os.listdir():
        end = f[-3:]
        if end == 'gif':
            l = f.split('_')
            os.rename(f, 'main_' + l[1] + '.' + end)
    
