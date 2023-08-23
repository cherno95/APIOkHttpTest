// Получаем имя ветки из переменной окружения TEST_BRANCH_NAME
task_branch = "${TEST_BRANCH_NAME}"
// Обрезаем имя ветки до нужного формата (убираем префикс origin, если есть)
def branch_cutted = task_branch.contains("origin") ? task_branch.split('/')[1] : task_branch.trim()
// Устанавливаем имя ветки как название сборки
currentBuild.displayName = "$branch_cutted"
// Устанавливаем URL вашего репозитория
base_git_url = "https://github.com/cherno95/APIOkHttpTest.git"

// Запускаем выполнение на узле Jenkins
node {
    // Устанавливаем переменные среды, которые будут доступны внутри этого блока
    withEnv(["branch=${branch_cutted}", "base_url=${base_git_url}"]) {
        // Этап "Checkout Branch" - клонируем репозиторий в соответствии с веткой
        stage("Checkout Branch") {
            // Проверяем, не является ли ветка "test"
            if (!"$branch_cutted".contains("test")) {
                try {
                    // Получаем код проекта для указанной ветки
                    getProject("$base_git_url", "$branch_cutted")
                } catch (err) {
                    // Выводим сообщение об ошибке и завершаем с ошибкой
                    echo "Failed get branch $branch_cutted"
                    throw ("${err}")
                }
            } else {
                echo "Current branch is test"
            }
        }

        // Этап "Run tests" - запускаем тесты с тегом "apiTest"
        stage("Run tests") {
            try {
                // Запускаем тесты с тегом "apiTest"
                runTestWithTag("apiTest")
            } finally {
                // Этап "Allure" - генерируем отчет Allure
                stage ("Allure") {
                    generateAllure()
                }
            }
        }
    }
}

// Функция для запуска тестов с указанным тегом
def runTestWithTag(String tag) {
    try {
        // Выполняем команду для запуска тестов с заданным тегом
        labelledShell(label: "Run ${tag}", script: "chmod +x gradlew \n./gradlew test -Ptag=${tag} -i")
    } finally {
        // Выводим сообщение в случае возникновения ошибок
        echo "some failed tests"
    }
}

// Функция для клонирования репозитория
def getProject(String repo, String branch) {
    cleanWs()
    checkout scm: [
            $class           : 'GitSCM', branches: [[name: branch]],
            userRemoteConfigs: [[
                                        url: repo
                                ]]
    ]
}

// Функция для генерации отчета Allure
def generateAllure() {
    allure([
            includeProperties: true,
            jdk              : '',
            properties       : [],
            reportBuildPolicy: 'ALWAYS',
            results          : [[path: 'build/allure-results']]
    ])
}
