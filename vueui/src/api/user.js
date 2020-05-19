import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/dologin',
    method: 'post',
    data
  })
}
export function getInfo(token) {
  return request({
    // url: '/getrouters',
    url: '/getinfo',
    method: 'get',
    params: { token }
  })
}
export function generateRoutes(token) {
  return request({
    url: '/getrouters',
    method: 'get',
    params: { token }
  })
}
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}
