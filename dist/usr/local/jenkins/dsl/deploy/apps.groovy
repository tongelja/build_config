package deploy

import common.Ansible
import common.Git

projects = [
    'hello-world': [envs: ['qa', 'prod']]
]

folder('deploy')
folder('deploy/apps')
projects.each { project, attribs ->
    folder("deploy/apps/${project}")
    attribs['envs'].each { env ->
        jobName = "deploy/apps/${project}/${env}"
        def deployJob = job(jobName) {
            logRotator { numToKeep(5) }
        }
        Ansible.addAnsiblePlaybook(deployJob, "dist/usr/local/jenkins/playbooks/deploy/apps${project}/${env}.yaml".toString(), true)
        Git.addGitScm(deployJob, "https://github.com/tongelja/build_config.git", "master")
        Git.addGitParameter(deployJob)
    }
}
