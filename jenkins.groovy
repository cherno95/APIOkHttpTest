// Значение переменной task_branch устанавливается из переменной TEST_BRANCH_NAME
task_branch = "${TEST_BRANCH_NAME}"

// Если task_branch содержит "origin", то branch_cutted устанавливается как часть после первого слеша, иначе удаляются пробелы вокруг task_branch
def branch_cutted = task_branch.contains("origin") ? task_branch.split('/')[1] : task_branch.trim()

// Устанавливается отображаемое имя текущей сборки на значение branch_cutted
currentBuild.displayName = "$branch_cutted"

// Задается базовый URL для git репозитория
base_git_url = "https://github.com/cherno95/APIOkHttpTest.git"

// Начинается выполнение pipeline на узле
node {
    // Создание переменных среды (окружения) для использования внутри блока
    withEnv(["branch=${branch_cutted}", "base_url=${base_git_url}"]) {

        // Начало этапа "Checkout Branch"
        stage("Checkout Branch") {
            // Проверяем, не содержит ли branch_cutted "test"
            if (!"$branch_cutted".contains("test")) {
                try {
                    // Вызываем функцию getProject для получения кода из git репозитория
                    getProject("$base_git_url", "$branch_cutted")
                } catch (err) {
                    // В случае ошибки выводим сообщение и прерываем выполнение с ошибкой
                    echo "Failed get branch $branch_cutted"
                    throw ("${err}")
                }
            } else {
                // Если branch_cutted содержит "test", выводим сообщение
                echo "Current branch is test"
            }
        }

        // Начало блока try
        try {
            // Вызываем функцию runTestWithTag для запуска тестов с заданным тегом
            runTestWithTag("apiTests")
        } finally {
            // Внутри блока finally выполняется действие независимо от успешности try блока
            stage ("Allure") {
                // Вызываем функцию generateAllure для создания отчетов в формате Allure
                generateAllure()
            }
        }
    }
}

// Определяем функцию runTestWithTag, которая принимает тег и выполняет тесты с этим тегом
def runTestWithTag(String tag) {
    try {
        // Вызываем функцию labelledShell для запуска тестов с заданным тегом
        labelledShell(label: "Run ${tag}", script: "./gradlew clean -x test ${tag}")
    } finally {
        // В блоке finally выводим сообщение о неудачных тестах
        echo "some failed tests"
    }
}

// Определяем функцию getProject для получения кода из git репозитория
def getProject(String repo, String branch) {
    // Очищаем рабочее пространство
    cleanWs()
    // Клонируем репозиторий с указанным URL и выбранной веткой
    checkout scm: [
            $class           : 'GitSCM', branches: [[name: branch]],
            userRemoteConfigs: [[
                                        url: repo
                                ]]
    ]
}

// Определяем функцию generateAllure для создания отчетов в формате Allure
def generateAllure() {
    allure([
            includeProperties: true,
            jdk              : '',
            properties       : [],
            reportBuildPolicy: 'ALWAYS',
            results          : [[path: 'build/allure-results']]
    ])
}
