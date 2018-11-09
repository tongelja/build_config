package common

class Ansible {
    static def playbookRoot = 'open/usr/local/jenkins/playbooks'

    static void addAnsiblePlaybook(def job, def playbookPath, def fromWorkspace=false, def dryRun=false) {
        job.with{
            steps {
                ansiblePlaybookBuilder{
                    ansibleName('Ansible')
                    if (fromWorkspace) {
                        playbook(playbookPath)
                    } else {
                        playbook("${playbookRoot}${playbookPath}".toString())
                    }
                    forks(100)
                    unbufferedOutput(true)
                    colorizedOutput(true)
                    disableHostKeyChecking(true)
                    sudo(false)
                    inventory{}       
                    additionalParameters(dryRun ? "--check" : "" ) 
                }
            }
            wrappers{ colorizeOutput() }
        }
    } 
}

