echo Creating service account for Github Actions
oc project c2mvws-tools

oc create serviceaccount github-actions

oc policy add-role-to-user edit system:serviceaccount:c2mvws-tools:github-actions

oc policy add-role-to-user edit system:serviceaccount:c2mvws-tools:github-actions -n c2mvws-dev

echo Retrieving the token of service account
oc serviceaccounts get-token github-actions