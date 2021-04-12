const postgres = require('postgres')
const crypto = require('crypto')
const { getUser } = require('../controller/usercontroller')

const sql = postgres({
    host: '127.0.0.1',
    port: 5432,
    database: 'flappybird',
    username: 'flappy-user',
    password: 'flappy-user'
})

exports.addScore = async function(usertoken, score) {
    try {
        let user = await exports.getUser(usertoken)

        let data = await sql`
            insert into public."score"(user_id, score)
            values(${user.id}, ${score})
            
            returning *
        `

        console.log(data)

        if (data.count == 0) {
            throw new Error('Could not add user to the database.')
        }

        return {
            usertoken: usertoken,
            score: score,
            result: true,
            error: ''
        }
    } catch (err) {
        console.log(err)
        
        return {
            usertoken: '',
            score: '',
            result: false,
            error: err.message
        }
    }
}

exports.getScore = async function(usertoken) {
    try {
        const data = await sql`
            select score 
            from public."score" s
            inner join public."user" u on u.id = s.user_id
            where u.token = ${ usertoken }
        `

        console.log(data);

        if (data.count <= 0) {
            throw new Error('No data was retrieved from the database')
        }

        return {
            usertoken: usertoken,
            score: data[0].score,
            result: true,
            error: ''
        }
    } catch (err) {
        console.log(err)
        return {
            usertoken: usertoken,
            result: false,
            score: -1,
            error: err.message
        }
    }
}

exports.deleteScore = async function(usertoken) {
    try {
        let user = await exports.getUser(usertoken)

        let data = await sql`
            delete from public."score"
            where user_id = ${user.id}
            
            returning *
        `

        console.log(data)

        if (data.count == 0) {
            throw new Error('Could not delete score from the database.')
        }

        return {
            usertoken: usertoken,
            result: true,
            error: ''
        }
    } catch (err) {
        console.log(err)
        
        return {
            usertoken: '',
            result: false,
            error: err.message
        }
    }
}

exports.updateScore = async function(usertoken, score) {
    try {
        let user = await exports.getUser(usertoken)

        let data = await sql`
            update public."score"
            set score = ${score}
            from public."score" s
            inner join public."user" u on u.id = s.user_id
            where u.token = ${user.usertoken}
            
            returning *
        `

        console.log(data)

        if (data.count == 0) {
            throw new Error('Could not update score in the database.')
        }

        return {
            usertoken: usertoken,
            score: score,
            result: true,
            error: ''
        }
    } catch (err) {
        console.log(err)
        
        return {
            usertoken: '',
            score: '',
            result: false,
            error: err.message
        }
    }
}

exports.addUser = async function(username, password) {
    try {
        let token = crypto.createHash('md5').update(username + '|' + password).digest('hex')

        let data = await sql`
            insert into public."user"(username, token)
            values(${username}, ${token})
            
            returning *
        `

        console.log(data)

        if (data.count == 0) {
            throw new Error('Could not add user to the database.')
        }

        return {
            usertoken: token,
            result: true,
            error: ''
        }
    } catch (err) {
        console.log(err)
        
        return {
            usertoken: '',
            result: false,
            error: err.message
        }
    }
}

exports.getUser = async function(usertoken) {
    try {
        let data = await sql`
            select id, username
            from public."user"
            where token = ${usertoken}
        `

        console.log(data)

        if (data.count == 0) {
            throw new Error('User was not found in the database')
        }

        return {
            username: data[0].username,
            usertoken: usertoken,
            result: true,
            error: ''
        }
    } catch (err) {
        console.log(err)

        return {
            id: '',
            username: '',
            usertoken: '',
            result: false,
            error: err.message
        }
    }
}

exports.deleteUser = async function(usertoken) {
    try {
        let data = await sql`
            delete from public."user"
            where token = ${usertoken}
            
            returning *
        `

        console.log(data)

        if (data.count == 0) {
            throw new Error('Could not delete user from the database.')
        }

        return {
            usertoken: usertoken,
            result: true,
            error: ''
        }
    } catch (err) {
        console.log(err)
        
        return {
            usertoken: '',
            result: false,
            error: err.message
        }
    }
}

exports.updateUser = async function(usertoken, username, password) {
    try {
        let newtoken = crypto.createHash('md5').update(username + '|' + password).digest('hex')
        
        let user = await exports.getUser(usertoken)

        let data = await sql`
            update public."user"
            set username = ${username}, token = ${newtoken}
            where id = ${user.id}
            
            returning *
        `

        console.log(data)

        if (data.count == 0) {
            throw new Error('Could not update score in the database.')
        }

        return {
            usertoken: usertoken,
            score: data[0].score,
            result: true,
            error: ''
        }
    } catch (err) {
        console.log(err)
        
        return {
            usertoken: '',
            score: '',
            result: false,
            error: err.message
        }
    }
}