# JetBrainsHW
Выполнено основное задание.
Программа работает в 2 потока(запускать 2 раза scan одновременно нельзя)
Команда exit реализована
Программа конкретно обрабатывает неправильно введённые параметры и выводит ошибку с описанием проблемы
Элементарный Unit-тест написан

Примеры входных данных:

scan -input "input" -output "output" -mask "*test*" -waitInterval 5000 -includeSubfolder false -autoDelete true

exit(для остановки сканнера, но главный поток при этом работает и ждёт дальнейших указаний)

scan -input "input?" -output "output?" -mask "*test*+" -waitInterval -5000 -includeSubfolder ыв -autoDelete фы

При этом программа выведет

Scanner was not started due to following reasons:
Illegal symbols in input path: input?
Illegal symbols in output path: output?
Illegal symbols in mask: *test*+
Incorrect value for waitInterval: -5000(Must be possitive int value)
Invalid value for IncludeSubfolder: ыв(Must be true or false)
Invalid value for autoDelete: фы(Must be true or false)
