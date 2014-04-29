import logging

__SERVER_SECRET_KEY = '211091945be5c4ef85834c6da082a8293ed2b9b0061ad1c09d33c32b65ebf873'.decode(
    'hex')

from datetime import datetime
from datetime import timedelta
from calendar import timegm
from uuid import uuid4
from json import dumps, loads

from Crypto.Cipher import AES
from Crypto.Util import Counter
from Crypto.Hash import HMAC


def create_token(applicationKey):
    expiration_data = datetime.now() + timedelta(minutes=15)
    expiration_timestamp = timegm(expiration_data.utctimetuple())
    message = {'id': uuid4().hex, 'expiration': expiration_timestamp,
               'applicationKey': applicationKey}
    data = dumps(message, separators=(',', ':'))
    ctr = Counter.new(128)
    cipher = AES.new(__SERVER_SECRET_KEY, AES.MODE_CTR, counter=ctr)
    return cipher.encrypt(data).encode('hex')


def verify_token(token):
    try:
        ctr = Counter.new(128)
        cipher = AES.new(__SERVER_SECRET_KEY, AES.MODE_CTR, counter=ctr)
        data = cipher.decrypt(token.decode('hex'))
        message = loads(data)
        logging.info(message)
        if message.get('expiration', -1) < timegm(datetime.now().utctimetuple()):
            return False
        return message.get('applicationKey', False)
    except Exception as e:
        logging.info(e.message)
        return False


def create_mac(secret, message):
    hasher = HMAC.new(secret)
    hasher.update(message)
    return hasher.hexdigest()

