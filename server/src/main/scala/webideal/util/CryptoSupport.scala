package webideal
package util

import com.typesafe.config.ConfigFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import java.util.Base64

// Some of this referenced from https://bitbucket.org/binarycamp/spray-contrib
trait CryptoSupport {
  import CryptoSupport._
  
  def config: Config
  
  private val key = {
    val keySpec = new PBEKeySpec(config.secret.toCharArray, Array.apply(0), config.iterationCount, config.keyLength)
    val secretKey = SecretKeyFactory.getInstance(config.secretKeyFactoryAlgorithm).generateSecret(keySpec)
    new SecretKeySpec(secretKey.getEncoded, config.algorithm)
  }
  
  def encrypt(str: String): String = Base64.getEncoder.encodeToString(encrypt(str.getBytes("UTF-8")))
  def encrypt(bytes: Array[Byte]): Array[Byte] = doFinal(bytes, Cipher.ENCRYPT_MODE)
  
  def decrypt(str: String): String = new String(decrypt(Base64.getDecoder.decode(str)), "UTF-8")
  def decrypt(bytes: Array[Byte]): Array[Byte] = doFinal(bytes, Cipher.DECRYPT_MODE)
  
  private def doFinal(data: Array[Byte], mode: Int): Array[Byte] = {
    val cipher = Cipher.getInstance(config.transformation)
    cipher.init(mode, key)
    cipher.doFinal(data)
  }
}
object CryptoSupport extends CryptoSupport {
  lazy val config = Config()
  
  case class Config(
    algorithm: String,
    transformation: String,
    secret: String,
    secretKeyFactoryAlgorithm: String,
    iterationCount: Int,
    keyLength: Int
  )
  object Config {
    def apply(): Config = apply(ConfigFactory.load().getConfig("webideal.crypto"))
    def apply(conf: com.typesafe.config.Config): Config = Config(
      algorithm = conf.getString("algorithm"),
      transformation = conf.getString("transformation"),
      secret = conf.getString("secret"),
      secretKeyFactoryAlgorithm = conf.getString("secret-key-factory-algorithm"),
      iterationCount = conf.getInt("iteration-count"),
      keyLength = conf.getInt("key-length")
    )
  }
}