package common

class Git {
    static void addGitParameter(def job, def defaultRef='') {
        job.with {
            parameters {
                stringParam(
                    'VERSION',
                    defaultRef,
                    'Name of tag or branch to deploy')
            }
        }
    }
    
    static void addGitScm(def job, def repoUrl, def branchName='${VERSION}', def restriction='') {
        job.with {
            scm {
                git {
                    remote {  
                        url(repoUrl)
                    }
                    branch(branchName)
                    configure { node ->
                        node / 'extensions' << 'hudson.plugins.git.extensions.impl.PathRestriction' {
                            includeRegions(restriction)
                            excludeRegions('')
                    }
                }
            }
        }
    }

    statis def branchesForRepo(def repo) {
        def branchCommand = 'git ls-remote --heads ${repo}"
        def proc = branchCommand.execute()
        proc.waitFor()
        return proc.in.text.split('\\n').collect{ it.split('/'[-1] }.findAll { it =~/^[0-9]+[0-9]+[0-9]+$/}
    }
}

